package com.admin.ezpark.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.admin.ezpark.databinding.SelectBottomSheetLayoutBinding
import com.admin.ezpark.enums.BottomSheetSelection
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SelectBottomSheet : BottomSheetDialogFragment() {

    var callback : ((BottomSheetSelection) -> Unit?)? = null

    private val binding: SelectBottomSheetLayoutBinding by lazy {
        SelectBottomSheetLayoutBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        events()
        return binding.root
    }

    private fun events(){
        binding.apply {
            onCameraCV.setOnClickListener {
                callback!!.invoke(BottomSheetSelection.SELECT_CAMERA)
                dialog!!.dismiss()
            }
            onGalleryCV.setOnClickListener {
                callback!!.invoke(BottomSheetSelection.SELECT_GALLERY)
                dialog!!.dismiss()
            }
            dismiss.setOnClickListener {
                dialog!!.dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            dialog.apply {
                this!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window!!.setLayout (
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT
                    )
                setCancelable(true)
            }

        }
    }


}