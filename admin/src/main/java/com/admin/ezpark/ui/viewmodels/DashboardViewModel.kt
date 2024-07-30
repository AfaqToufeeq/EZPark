package com.admin.ezpark.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin.ezpark.data.local.repository.DashboardRepository
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

    private val _dashboardCards = MutableLiveData<List<DashboardCard>>()
    val dashboardCards: LiveData<List<DashboardCard>> get() = _dashboardCards

    init {
        loadDashboardCards()
    }

    private fun loadDashboardCards() {
        viewModelScope.launch(dispatcherProvider.io) {
            _dashboardCards.postValue(repository.getDashboardCards())
        }

    }
}
