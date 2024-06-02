package com.example.studybuddy.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.locale.DataStoreManager
import com.example.studybuddy.data.model.UserRequest
import com.example.studybuddy.domain.auth.GetUserUseCase
import com.example.studybuddy.domain.auth.LogoutUseCase
import com.example.studybuddy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase, private val getUserUseCase: GetUserUseCase, private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _logoutResult = MutableStateFlow<Resource<Unit>?>(null)
    val logoutResult: StateFlow<Resource<Unit>?> = _logoutResult

    fun logout() {
        viewModelScope.launch {
            logoutUseCase().collect {
                _logoutResult.value = it
            }
        }
    }

    fun getUser(request: UserRequest) {
        // TODO
        viewModelScope.launch {
            getUserUseCase(request).collect {
               // TODO
            }
        }
    }

    fun clearToken() {
        runBlocking {
            dataStoreManager.clearToken()
        }
    }
}
