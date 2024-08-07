package com.admin.ezpark.ui.viewmodels

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin.ezpark.data.models.OwnerModel
import com.admin.ezpark.data.models.ParkingLotModel
import com.admin.ezpark.data.remote.firebase.FirebaseStorageManager
import com.admin.ezpark.data.repository.ParkingLotRepository
import com.admin.ezpark.enums.ImageFolder
import com.admin.ezpark.utils.CoroutineDispatcherProvider
import com.admin.ezpark.utils.Utils.generateUniqueId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParkingLotViewModel @Inject constructor(
    private val repository: ParkingLotRepository,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val firebaseStorageManager: FirebaseStorageManager
) : ViewModel() {

    private val _result = MutableLiveData<Boolean>()
    val result: LiveData<Boolean> get() = _result

    private val _ownerInfo = MutableLiveData<Triple<List<OwnerModel>, Boolean, String>>()
    val ownerInfo: LiveData<Triple<List<OwnerModel>, Boolean, String>> get() = _ownerInfo

    private val _parkingLotInfo = MutableLiveData<ParkingLotModel>()
    val parkingLotInfo: LiveData<ParkingLotModel> get() = _parkingLotInfo

    init {
        getOwnerInfo()
    }

    fun saveOwnerInfo(owner: OwnerModel) {
        viewModelScope.launch(dispatcherProvider.io) {
            val imageID = generateUniqueId()
            val imageFileId = "${ImageFolder.OWNERS.path}${imageID}.png"
            val imageUriResult =
                owner.profileImageUrl?.let { firebaseStorageManager.uploadImage(imageFileId, it.toUri()) }
            owner.apply { profileImageUrl = imageUriResult.toString() }
           val result = repository.addOwner(owner)

            if (result.isSuccess)
                _result.postValue(true)
            else
                _result.postValue(false)
        }
    }


    private fun getOwnerInfo() {
        viewModelScope.launch(dispatcherProvider.io) {
            repository.getOwnerInfo { ownerList, flag, msg->
                _ownerInfo.postValue(Triple(ownerList, flag, msg))
            }
        }
    }
}