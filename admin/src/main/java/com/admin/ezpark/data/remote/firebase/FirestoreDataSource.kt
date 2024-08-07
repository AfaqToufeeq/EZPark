package com.admin.ezpark.data.remote.firebase

import com.admin.ezpark.callbacks.Identifiable
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreDataSource @Inject constructor(private val firestore: FirebaseFirestore) {

    suspend fun <T : Any> createDocumentWithAutoId(collectionPath: String, data: T): Result<String> {
        return try {
            // Get the collection reference
            val collection = firestore.collection(collectionPath)
            // Generate a new document reference with an auto-generated ID
            val documentRef = collection.document()
            val documentId = documentRef.id
            // Check if the data implements Identifiable and set the ID
            if (data is Identifiable) {
                data.id = documentId
            } else {
                // Use reflection to set the ID if data is not an Identifiable
                val idField = data::class.java.getDeclaredField("id")
                idField.isAccessible = true
                idField.set(data, documentId)
            }
            // Set the data in Firestore
            documentRef.set(data).await()
            // Return the generated document ID
            Result.success(documentId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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