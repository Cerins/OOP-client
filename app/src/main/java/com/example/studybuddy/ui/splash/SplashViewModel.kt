package com.example.studybuddy.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.locale.DataStoreManager
import com.example.studybuddy.domain.auth.LogoutUseCase
import com.example.studybuddy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _token = MutableStateFlow<Resource<String>?>(null)
    val logoutResult: StateFlow<Resource<String>?> = _token


    private fun checkDataStore() {
        viewModelScope.launch {
            dataStoreManager.clearDataStore()
        }
    }
}
