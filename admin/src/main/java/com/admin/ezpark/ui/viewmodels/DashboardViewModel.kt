package com.admin.ezpark.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin.ezpark.data.repository.DashboardRepository
import com.admin.ezpark.data.models.DashboardCard
import com.admin.ezpark.utils.CoroutineDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DashboardRepository,
    private val dispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {

    private val _dashboardCardsType1= MutableLiveData<List<DashboardCard>>()
    val dashboardCardsType1: LiveData<List<DashboardCard>> get() = _dashboardCardsType1

    private val _dashboardCardsType2 = MutableLiveData<List<DashboardCard>>()
    val dashboardCardsType2: LiveData<List<DashboardCard>> get() = _dashboardCardsType2

    init {
        getDashboardCardsType1()
        getDashboardCardsType2()
    }

    private fun getDashboardCardsType1() {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                _dashboardCardsType1.postValue(repository.getDashboardCardsType1())
            } catch (e: Exception) {
                // Handle error (e.g., post an error state to another LiveData)
                _dashboardCardsType1.postValue(emptyList())
            }
        }
    }

    private fun getDashboardCardsType2() {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                _dashboardCardsType2.postValue(repository.getDashboardCardsType2())
            } catch (e: Exception) {
                // Handle error (e.g.,post an error state to another LiveData)
                _dashboardCardsType2.postValue(emptyList())
            }
        }
    }
}
