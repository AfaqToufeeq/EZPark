package com.admin.ezpark.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin.ezpark.data.repository.AdminRepository
import com.admin.ezpark.utils.CoroutineDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val adminRepository: AdminRepository,
    private val dispatcherProvider: CoroutineDispatcherProvider
): ViewModel() {
    private val _loginStatus = MutableLiveData<Result<Unit>>()
    val loginStatus: LiveData<Result<Unit>> get() = _loginStatus

    fun loginAdminCredentials(loginID: String, password: String) {
        viewModelScope.launch(dispatcherProvider.io) {
            val result = adminRepository.loginAdminCredentials(loginID, password)
            _loginStatus.postValue(result)
        }
    }
}