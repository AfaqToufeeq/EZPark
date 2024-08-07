package com.admin.ezpark.data.remote.firebase

import android.content.Context
import android.net.Uri
import com.admin.ezpark.utils.Utils.showToast
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseStorageManager @Inject constructor(
    private val mStorageRef: StorageReference,
    private val context: Context) {

    suspend fun uploadImage(imageFileId: String, imageUri: Uri): Uri? {
        return try {
            mStorageRef.child(imageFileId).putFile(imageUri).await()
            val uri = mStorageRef.child(imageFileId).downloadUrl.await()
            uri
        } catch (e: Exception) {
            handleError(e.message)
            null
        }
    }

    suspend fun deleteFile(filePath: String): Result<Unit> {
        return try {
            mStorageRef.child(filePath).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun handleError( errorMessage: String?) {
       showToast(context, errorMessage.toString())
    }

}