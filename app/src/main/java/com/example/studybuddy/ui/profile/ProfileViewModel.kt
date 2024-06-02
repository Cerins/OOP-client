package com.example.studybuddy.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.locale.DataStoreManager
import com.example.studybuddy.data.model.User
import com.example.studybuddy.domain.auth.GetFriendsUseCase
import com.example.studybuddy.domain.auth.GetUserUseCase
import com.example.studybuddy.domain.auth.LogoutUseCase
import com.example.studybuddy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val userUseCase: GetUserUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _logoutResult = MutableStateFlow<Resource<Unit>?>(null)
    val logoutResult: StateFlow<Resource<Unit>?> = _logoutResult

    private val _userResult = MutableStateFlow<Resource<User>?>(null)
    val userResult: StateFlow<Resource<User>?> = _userResult

    fun logout() {
        viewModelScope.launch {
            logoutUseCase().collect {
                _logoutResult.value = it
            }
        }
    }

    fun loadUserId() {
        viewModelScope.launch {
            val userId = dataStoreManager.userFlow.first()
            userId?.let {
                getUser(it)
            }
        }
    }

    private fun getUser(id: Int) {
        viewModelScope.launch {
            userUseCase(id).collect { result ->
                _userResult.value = result
            }
        }
    }

    fun clearDataStore() {
        runBlocking {
            dataStoreManager.clearDataStore()
        }
    }
}
