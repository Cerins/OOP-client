package com.example.studybuddy.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.locale.DataStoreManager
import com.example.studybuddy.data.model.LoginResponse
import com.example.studybuddy.domain.auth.LoginUseCase
import com.example.studybuddy.domain.auth.LogoutUseCase
import com.example.studybuddy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _loginResult = MutableStateFlow<Resource<LoginResponse>?>(null)
    val loginResult: StateFlow<Resource<LoginResponse>?> = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginUseCase(email, password).collect { result ->
                    _loginResult.value = result
                    if (result is Resource.Success) {
                        saveToken(result.data?.token)
                        saveUserId(result.data?.user?.id)
                    }
            }
        }
    }

    private fun saveToken(token: String?) {
        runBlocking {
            token?.let {
                dataStoreManager.saveToken(it)
            }
        }
    }

    private fun saveUserId(userId: Int?) {
        runBlocking {
            userId?.let {
                dataStoreManager.saveUserId(it)
            }
        }
    }

}
