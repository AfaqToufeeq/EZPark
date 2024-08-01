package com.admin.ezpark.data.repository

import com.admin.ezpark.data.remote.firebase.FirestoreDataSource
import com.admin.ezpark.enums.LogLevel
import com.admin.ezpark.utils.Utils
import javax.inject.Inject

class AdminRepository @Inject constructor(private val firestoreDataSource: FirestoreDataSource) {

    suspend fun loginAdminCredentials(loginID: String, password: String): Result<Unit> {
        return try {
            val documentResult = firestoreDataSource.getDocument("admins", "admin")
            documentResult.fold(
                onSuccess = { document ->
                    document?.data?.let { data ->
                        val storedLoginID = data["loginID"] as? String
                        val storedPassword = data["password"] as? String

                        return if (storedLoginID == loginID && storedPassword == password) {
                            Utils.log(LogLevel.DEBUG, "Login successful: $loginID")
                            Result.success(Unit)
                        } else {
                            Utils.log(LogLevel.DEBUG, "Invalid credentials for $loginID")
                            Result.failure(Exception("Invalid credentials"))
                        }
                    } ?: run {
                        Utils.log(LogLevel.DEBUG, "No such document")
                        Result.failure(Exception("No such document"))
                    }
                },
                onFailure = { exception ->
                    Result.failure(exception)
                }
            )
        } catch (e: Exception) {
            Utils.log(LogLevel.ERROR, "Error fetching admin document: ${e.message}")
            Result.failure(e)
        }
    }
}