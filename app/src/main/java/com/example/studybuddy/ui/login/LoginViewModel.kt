package com.example.studybuddy.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.locale.DataStoreManager
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
    private val logoutUseCase: LogoutUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _loginResult = MutableStateFlow<Resource<String>?>(null)
    val loginResult: StateFlow<Resource<String>?> = _loginResult

    private val _logoutResult = MutableStateFlow<Result<Unit>?>(null)
    val logoutResult: StateFlow<Result<Unit>?> = _logoutResult


    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginUseCase(email, password).collect { result ->
                    _loginResult.value = result
            }
        }
    }

    fun saveToken(token: String?) {
        runBlocking {
            token?.let {
                dataStoreManager.saveToken(it)
            }
        }
    }

//    fun logout() {
//        viewModelScope.launch {
//            val result = logoutUseCase()
//            _logoutResult.value = result
//        }
//    }

}
