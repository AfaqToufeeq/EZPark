package com.admin.ezpark.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.admin.ezpark.data.models.OwnerModel
import com.admin.ezpark.data.models.ParkingLotModel
import com.admin.ezpark.data.repository.ParkingLotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ParkingLotViewModel @Inject constructor(private val repository: ParkingLotRepository) : ViewModel() {
    private val _ownerInfo = MutableLiveData<OwnerModel>()
    val ownerInfo: LiveData<OwnerModel> get() = _ownerInfo

    private val _parkingLotInfo = MutableLiveData<ParkingLotModel>()
    val parkingLotInfo: LiveData<ParkingLotModel> get() = _parkingLotInfo
}