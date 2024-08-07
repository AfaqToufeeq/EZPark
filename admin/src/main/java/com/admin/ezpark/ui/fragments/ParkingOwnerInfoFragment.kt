package com.admin.ezpark.ui.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.admin.ezpark.R
import com.admin.ezpark.data.models.OwnerModel
import com.admin.ezpark.data.remote.firebase.FirebaseStorageManager
import com.admin.ezpark.databinding.FragmentParkingOwnerInfoBinding
import com.admin.ezpark.enums.BottomSheetSelection
import com.admin.ezpark.enums.Gender
import com.admin.ezpark.ui.viewmodels.ParkingLotViewModel
import com.admin.ezpark.utils.CoroutineDispatcherProvider
import com.admin.ezpark.utils.ImagePicker
import com.admin.ezpark.utils.SelectBottomSheet
import com.admin.ezpark.utils.Utils
import com.admin.ezpark.utils.Utils.isValidEmail
import com.admin.ezpark.utils.Utils.isValidPhone
import com.admin.ezpark.utils.Utils.showToast
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class ParkingOwnerInfoFragment : BaseFragment() {

    private var _binding: FragmentParkingOwnerInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ParkingLotViewModel by viewModels()
    @Inject lateinit var dispatcherProvider: CoroutineDispatcherProvider
    @Inject lateinit var firebaseStorageManager: FirebaseStorageManager
    private lateinit var loader: Dialog

    private var imageUri: Uri? = null

    private val pickImageContract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let {
            imageUri = it
            binding.profileImage.setImageURI(it)
        }
    }

    private val takePictureContract =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                binding.profileImage.setImageURI(null)
                binding.profileImage.setImageURI(imageUri)
            } else {
                showToast(requireActivity(), "Failed to capture image")
            }
        }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentParkingOwnerInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupListeners()
        setObservers()
    }

    private fun initViews() {
        loader = Utils.progressDialog(requireActivity())
    }

    private fun setObservers() {
        viewModel.result.observe(viewLifecycleOwner){
            if (it) {
                showToast(requireActivity(), "Owner is added")
                navigateBack()
            } else {
                showToast(requireActivity(), "Failed to add owner")
            }

            if (loader.isShowing) loader.dismiss()
        }
    }

    private fun setupListeners() {
        with(binding) {
            toolbar.backIV.setOnClickListener { navigateBack() }
            btnAddOwner.setOnClickListener { validateAndSubmitOwnerData() }
            uploadImageIV.setOnClickListener { showImageSelectionBottomSheet() }
            dobEditText.setOnClickListener { showDatePicker() }
        }
    }


    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }.time
                updateDateOfBirth(selectedDate)
            },
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
        datePicker.show()
    }

    private fun updateDateOfBirth(date: Date) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        binding.dobInputLayout.editText?.setText(dateFormat.format(date))
    }

    private fun showImageSelectionBottomSheet() {
        SelectBottomSheet().apply {
            callBack = { selection ->
                when (selection) {
                    BottomSheetSelection.SELECT_CAMERA -> {
                        ImagePicker.checkPermissionsAndPickImage(this@ParkingOwnerInfoFragment, takePictureContract, dispatcherProvider) { imgUri ->
                            imageUri = imgUri
                        }
                    }
                    BottomSheetSelection.SELECT_GALLERY -> {
                        ImagePicker.selectGalleryImage(pickImageContract)
                    }
                }
            }
        }.show(childFragmentManager, "SelectBottomSheet")
    }


    private fun validateAndSubmitOwnerData() {
        if (!loader.isShowing) loader.show()

        if (validateForm()) {
            val ownerData = collectOwnerData()
            viewModel.saveOwnerInfo(ownerData)
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        with(binding) {
            isValid = isValidField(ownerNameInputLayout, ownerNameInputLayout.editText?.text.toString().trim()) && isValid
            isValid = isValidField(dobInputLayout, dobInputLayout.editText?.text.toString().trim()) && isValid
            isValid = isValidField(cnicPassportInputLayout, cnicPassportInputLayout.editText?.text.toString().trim()) && isValid
            isValid = isValidEmail(emailInputLayout, emailInputLayout.editText?.text.toString().trim()) && isValid
            isValid = isValidPhone(phoneInputLayout, phoneInputLayout.editText?.text.toString().trim()) && isValid
            isValid = isValidField(addressInputLayout, addressInputLayout.editText?.text.toString().trim()) && isValid
            isValid = isValidImageUri(imageUri) && isValid

            if (radioGroupGender.checkedRadioButtonId == -1) {
                showToast(requireActivity(), getString(R.string.error_select_gender))
                isValid = false
            }
        }

        return isValid
    }

    private fun isValidField(inputLayout: TextInputLayout, value: String): Boolean {
        return if (value.isEmpty()) {
            inputLayout.error = getString(R.string.error_required_field)
            false
        } else {
            inputLayout.error = null
            true
        }
    }

    private fun isValidEmail(inputLayout: TextInputLayout, value: String): Boolean {
        return if (value.isEmpty() || !value.isValidEmail()) {
            inputLayout.error = getString(R.string.error_invalid_email)
            false
        } else {
            inputLayout.error = null
            true
        }
    }

    private fun isValidPhone(inputLayout: TextInputLayout, value: String): Boolean {
        return if (value.isEmpty() || !value.isValidPhone()) {
            inputLayout.error = getString(R.string.error_invalid_phone)
            false
        } else {
            inputLayout.error = null
            true
        }
    }

    private fun isValidImageUri(imageUri: Uri?): Boolean{
        return when (imageUri) {
            null -> {
                showToast(requireActivity(), getString(R.string.error_required_image))
                false
            }
            else -> {
                true
            }
        }
    }

    private fun collectOwnerData(): OwnerModel {
        with(binding) {
            val ownerName = ownerNameInputLayout.editText?.text.toString().trim()
            val dob = dobInputLayout.editText?.text.toString().trim()
            val cnicPassport = cnicPassportInputLayout.editText?.text.toString().trim()
            val email = emailInputLayout.editText?.text.toString().trim()
            val phone = phoneInputLayout.editText?.text.toString().trim()
            val address = addressInputLayout.editText?.text.toString().trim()

            val selectedGenderId = radioGroupGender.checkedRadioButtonId
            val gender = when (view?.findViewById<RadioButton>(selectedGenderId)?.text) {
                getString(R.string.male) -> Gender.MALE
                getString(R.string.female) -> Gender.FEMALE
                else -> Gender.OTHER
            }

            return OwnerModel(
                ownerName = ownerName,
                gender = gender,
                dateOfBirth = dob,
                cnicOrPassport = cnicPassport,
                profileImageUrl = imageUri.toString(),
                email = email,
                phone = phone,
                address = address
            )
        }
    }

    private fun navigateBack() = findNavController().popBackStack()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
