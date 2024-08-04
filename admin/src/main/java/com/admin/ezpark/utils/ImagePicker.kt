package com.admin.ezpark.utils

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File

object ImagePicker {
    private const val REQUEST_PERMISSION_CAMERA = 100
    private const val REQUEST_PERMISSION_STORAGE = 101

    fun checkPermissionsAndPickImage(
        fragment: Fragment,
        takePictureLauncher: ActivityResultLauncher<Uri>,
        pickImageLauncher: ActivityResultLauncher<String>
    ) {
        when {
            ContextCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED -> {
                ActivityCompat.requestPermissions(
                    fragment.requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_PERMISSION_CAMERA
                )
            }
            ContextCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED -> {
                ActivityCompat.requestPermissions(
                    fragment.requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION_STORAGE
                )
            }
            else -> launchCamera(fragment, takePictureLauncher)
        }
    }

    private fun launchCamera(fragment: Fragment, takePictureLauncher: ActivityResultLauncher<Uri>) {
        val imageUri = createImageUri(fragment)
        imageUri?.let { takePictureLauncher.launch(it) }
    }

    private fun createImageUri(fragment: Fragment): Uri? {
        val image = File(fragment.requireActivity().applicationContext.filesDir, "camera_photo.png")
        return FileProvider.getUriForFile(fragment.requireActivity().applicationContext, "com.admin.ezpark.FileProvider", image)
    }

    fun selectGalleryImage(pickImageLauncher: ActivityResultLauncher<String>) {
        pickImageLauncher.launch("image/*")
    }

}