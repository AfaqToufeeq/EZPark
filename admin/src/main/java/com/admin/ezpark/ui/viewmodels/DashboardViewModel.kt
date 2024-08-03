package com.admin.ezpark.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin.ezpark.data.models.DashboardCompModel
import com.admin.ezpark.data.repository.DashboardRepository
import com.admin.ezpark.utils.CoroutineDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DashboardRepository,
    private val dispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {

    private val _dashboardCardsType1= MutableLiveData<DashboardCompModel>()
    val dashboardCardsType1: LiveData<DashboardCompModel> get() = _dashboardCardsType1

    private val _dashboardCardsType2 = MutableLiveData<DashboardCompModel>()
    val dashboardCardsType2: LiveData<DashboardCompModel> get() = _dashboardCardsType2

    private val _dashboardCardsType3 = MutableLiveData<DashboardCompModel>()
    val dashboardCardsType3: LiveData<DashboardCompModel> get() = _dashboardCardsType3


    init {
        getDashboardCardsType1()
        getDashboardCardsType2()
        getDashboardCardsType3()
    }

    private fun getDashboardCardsType1() {
        viewModelScope.launch(dispatcherProvider.io) {
            _dashboardCardsType1.postValue(repository.getDashboardCardsType1())
        }
    }

    private fun getDashboardCardsType2() {
        viewModelScope.launch(dispatcherProvider.io) {
            _dashboardCardsType2.postValue(repository.getDashboardCardsType2())
        }
    }

    private fun getDashboardCardsType3() {
        viewModelScope.launch(dispatcherProvider.io) {
            _dashboardCardsType3.postValue(repository.getDashboardCardsType3())
        }
    }
}
