package com.admin.ezpark.data.remote.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreDataSource @Inject constructor(private val firestore: FirebaseFirestore) {

    suspend fun getDocument(collectionPath: String, documentId: String): Result<DocumentSnapshot?> {
        return try {
            val document = firestore.collection(collectionPath).document(documentId).get().await()
            Result.success(document)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun <T> setDocument(collectionPath: String, documentId: String, data: T): Result<Void?> {
        return try {
            val result = firestore.collection(collectionPath).document(documentId).set(data!!).await()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCollection(collectionPath: String): Result<CollectionReference?> {
        return try {
            val collection = firestore.collection(collectionPath)
            Result.success(collection)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}