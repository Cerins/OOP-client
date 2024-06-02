package com.example.studybuddy.ui.register

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.locale.DataStoreManager
import com.example.studybuddy.data.model.RegisterRequest
import com.example.studybuddy.data.model.RegisterResponse
import com.example.studybuddy.domain.auth.LoginUseCase
import com.example.studybuddy.domain.auth.PickImageUseCase
import com.example.studybuddy.domain.auth.RegisterUseCase
import com.example.studybuddy.domain.auth.TakePhotoUseCase
import com.example.studybuddy.util.Event
import com.example.studybuddy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    application: Application,
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
    private val takePhotoUseCase: TakePhotoUseCase,
    private val pickImageUseCase: PickImageUseCase,
    private val dataStoreManager: DataStoreManager,
    ) : AndroidViewModel(application) {

    private val _registerResult = MutableStateFlow<Resource<RegisterResponse>?>(null)
    val registerResult: StateFlow<Resource<RegisterResponse>?> = _registerResult

    private val _loginResult = MutableStateFlow<Resource<String>?>(null)
    val loginResult: StateFlow<Resource<String>?> = _loginResult

    private val _permissionRequest = MutableStateFlow<Event<List<String>>?>(null)
    val permissionRequest: StateFlow<Event<List<String>>?> get() = _permissionRequest

    private val _intentFlow = MutableStateFlow<Event<Intent>?>(null)
    val intentFlow: StateFlow<Event<Intent>?> get() = _intentFlow

    private val _interests = MutableStateFlow(
        listOf(
            InterestChip("Matemātika", true),
            InterestChip("Latviešu val.", true),
            InterestChip("Ķīmĳa", true),
            InterestChip("+", false)
        )
    )
    val interests: StateFlow<List<InterestChip>> get() = _interests


    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            registerUseCase(request).collect{
                _registerResult.value = it
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            loginUseCase(username, password).collect { result ->
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



    fun onTakePhotoClicked() {
        viewModelScope.launch {
            takePhotoUseCase().collect { intent ->
                _intentFlow.value = Event(intent)
            }
        }
    }

    fun onPickImageClicked() {
        viewModelScope.launch {
            pickImageUseCase().collect { intent ->
                _intentFlow.value = Event(intent)
            }
        }
    }

    fun requestPermissions(permissions: List<String>) {
        _permissionRequest.value = Event(permissions)
    }

    fun clearIntent() {
        _intentFlow.update { null }
    }

    fun addInterest(interest: String) {
        if (interest.isNotBlank()) {
            _interests.update { currentList ->
                if (!currentList.map { it.name }.contains(interest)) {
                    currentList + InterestChip(interest)
                } else {
                    currentList
                }
            }
        }
    }

    fun removeInterest(interest: String) {
        _interests.update { currentList ->
            currentList - InterestChip(interest)
        }
    }

}

data class InterestChip(
    val name: String, val isCloseVisible: Boolean = true
)
