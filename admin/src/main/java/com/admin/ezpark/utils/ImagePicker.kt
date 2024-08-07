package com.admin.ezpark.utils

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.admin.ezpark.enums.LogLevel
import kotlinx.coroutines.launch
import java.io.File


object ImagePicker {
    private const val REQUEST_PERMISSION_CAMERA = 100
    private const val REQUEST_PERMISSION_STORAGE = 101

    fun checkPermissionsAndPickImage(
        fragment: Fragment,
        takePictureLauncher: ActivityResultLauncher<Uri>,
        dispatcherProvider: CoroutineDispatcherProvider,
        callback: (Uri?) -> Unit
    ) {
        when {
            Build.VERSION.SDK_INT > Build.VERSION_CODES.P -> {
                handleCameraPermission(fragment, takePictureLauncher, dispatcherProvider, callback)
            }
            else -> {
                handleLegacyPermissions(fragment, takePictureLauncher, dispatcherProvider, callback)
            }
        }
    }

    private fun handleCameraPermission(
        fragment: Fragment,
        takePictureLauncher: ActivityResultLauncher<Uri>,
        dispatcherProvider: CoroutineDispatcherProvider,
        callback: (Uri?) -> Unit
    ) {
        if (ContextCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                fragment.requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION_CAMERA
            )
        } else {
             launchCamera(fragment, takePictureLauncher, dispatcherProvider, callback)
        }
    }

    private fun handleLegacyPermissions(
        fragment: Fragment,
        takePictureLauncher: ActivityResultLauncher<Uri>,
        dispatcherProvider: CoroutineDispatcherProvider,
        callback: (Uri?) -> Unit
    ) { // Added return type
        if (ContextCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                fragment.requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_STORAGE
            )
        } else {
             launchCamera(fragment, takePictureLauncher, dispatcherProvider, callback)
        }
    }

    private fun launchCamera(
        fragment: Fragment,
        takePictureLauncher: ActivityResultLauncher<Uri>,
        dispatcherProvider: CoroutineDispatcherProvider,
        callback: (Uri?) -> Unit
    ) {
        var imageUri: Uri?
        fragment.lifecycleScope.launch(dispatcherProvider.io) {
            try {
                imageUri = createImageUri(fragment)
                imageUri?.let { takePictureLauncher.launch(it) }
                callback.invoke(imageUri)
            } catch (e: Exception) {
                Utils.log(LogLevel.ERROR, "Error creating image URI $e")
                Utils.showToast(fragment.requireActivity(), "Failed to open camera")
            }
        }
    }

    private fun createImageUri(fragment: Fragment): Uri? {
        val imageFile = File(fragment.requireActivity().applicationContext.filesDir, "camera_photo.png")
        return FileProvider.getUriForFile(fragment.requireActivity().applicationContext, "${fragment.requireActivity().applicationContext.packageName}.FileProvider", imageFile)
    }

    fun selectGalleryImage(pickImageLauncher: ActivityResultLauncher<String>) {
        pickImageLauncher.launch("image/*")
    }
}
