package com.admin.ezpark.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.admin.ezpark.R
import com.admin.ezpark.data.models.OwnerModel
import com.admin.ezpark.databinding.FragmentParkingOwnerInfoBinding
import com.admin.ezpark.enums.BottomSheetSelection
import com.admin.ezpark.enums.Gender
import com.admin.ezpark.enums.LogLevel
import com.admin.ezpark.ui.viewmodels.AdminViewModel
import com.admin.ezpark.utils.REQUEST_PERMISSION_CAMERA
import com.admin.ezpark.utils.REQUEST_PERMISSION_STORAGE
import com.admin.ezpark.utils.SelectBottomSheet
import com.admin.ezpark.utils.Utils
import com.admin.ezpark.utils.Utils.isValidEmail
import com.admin.ezpark.utils.Utils.isValidPhone
import com.admin.ezpark.utils.Utils.showToast
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class ParkingOwnerInfoFragment : BaseFragment() {

    private var _binding: FragmentParkingOwnerInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminViewModel by viewModels()

    private var imageUri: Uri? = null

    private val pickImageContract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let {
            imageUri = it
            binding.profileImage.setImageURI(it)
        }
    }

    private val takePictureContract = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        it?.let {
            binding.profileImage.setImageURI(null)
            binding.profileImage.setImageURI(imageUri)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentParkingOwnerInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        with(binding) {
            toolbar.backIV.setOnClickListener { navigateBack() }
            btnAddOwner.setOnClickListener { validateAndSubmitOwnerData() }
            uploadImageIV.setOnClickListener { showImageSelectionBottomSheet() }
        }
    }

    private fun showImageSelectionBottomSheet() {
        SelectBottomSheet().apply {
            callBack = { selection ->
                when (selection) {
                    BottomSheetSelection.SELECT_CAMERA -> checkPermissionsAndPickImage()
                    BottomSheetSelection.SELECT_GALLERY -> selectGalleryImage()
                }
            }
        }.show(childFragmentManager, "SelectBottomSheet")
    }

    private fun checkPermissionsAndPickImage() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Request camera permission if it is not granted
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION_CAMERA
            )
        } else {
            // Permission already granted, start the camera activity
            launchCamera()
        }
    }

    private fun launchCamera() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val deferredUri = async { createImageUri()}
                imageUri = deferredUri.await()
                takePictureContract.launch(imageUri)
            } catch (e: Exception) {
               Utils.log(LogLevel.ERROR, "Error creating image URI $e")
                showToast(requireActivity(),"Failed to open camera")
            }
        }
    }

    private fun createImageUri(): Uri? {
        val image = File(requireActivity().applicationContext.filesDir, "camera_photo.png")
        return FileProvider.getUriForFile(requireActivity().applicationContext, "com.admin.ezpark.FileProvider", image)
    }

    private fun selectGalleryImage() {
        pickImageContract.launch("image/*")
    }

    private fun validateAndSubmitOwnerData() {
        if (validateForm()) {
            val ownerData = collectOwnerData()
            // Handle the owner data (e.g., save to database, navigate to another screen)
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
                ownerId = "",
                ownerName = ownerName,
                gender = gender,
                dateOfBirth = dob,
                cnicOrPassport = cnicPassport,
                profileImageUrl = null,
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
