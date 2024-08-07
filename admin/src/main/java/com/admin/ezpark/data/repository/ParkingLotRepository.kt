package com.admin.ezpark.data.repository

import com.admin.ezpark.data.models.OwnerModel
import com.admin.ezpark.data.remote.firebase.FirestoreDataSource
import com.admin.ezpark.enums.FireStoreKeys
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class ParkingLotRepository @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource,
                                               private val firestore: FirebaseFirestore
){

    suspend fun addOwner(ownerModel: OwnerModel): Result<String> {
        return firestoreDataSource.createDocumentWithAutoId(FireStoreKeys.owners.toFormattedString(), ownerModel)
    }

    suspend fun updateOwner(ownerId: String, ownerModel: OwnerModel): Result<Void?> {
        return firestoreDataSource.setDocument(FireStoreKeys.owners.toFormattedString(), ownerId, ownerModel)
    }

    suspend fun getOwnerInfo(onComplete: (List<OwnerModel>, Boolean, String) -> Unit) {
        val ownersCollectionResult = firestoreDataSource.getCollection(FireStoreKeys.owners.toFormattedString())
        val ownersList = mutableListOf<OwnerModel>()

        ownersCollectionResult.onSuccess { snapshot ->
            try {
                snapshot?.get()?.await()?.documents?.forEach { document ->
                    val owner = document.toObject(OwnerModel::class.java)
                    ownersList.add(owner!!)
                    Timber.d("Owner: $owner")
                }
                onComplete.invoke(ownersList,true, "Success")
            } catch (e: Exception) {
                Timber.e(e, "Error fetching owner information")
                onComplete.invoke(emptyList(),false, e.message.toString())
            }
        }.onFailure { exception ->
            Timber.e(exception, "Failed to get owners collection")
            onComplete.invoke(emptyList(),false, exception.message.toString())
        }
    }
}
