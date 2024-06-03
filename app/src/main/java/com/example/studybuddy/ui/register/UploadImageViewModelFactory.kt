package com.example.studybuddy.ui.register

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studybuddy.data.locale.DataStoreManager
import com.example.studybuddy.domain.auth.LoginUseCase
import com.example.studybuddy.domain.auth.PickImageUseCase
import com.example.studybuddy.domain.auth.RegisterUseCase
import com.example.studybuddy.domain.auth.TakePhotoUseCase
import javax.inject.Inject

//class RegisterViewModelFactory @Inject constructor(
//    private val application: Application,
//    private val takePhotoUseCase: TakePhotoUseCase,
//    private val pickImageUseCase: PickImageUseCase,
//    private val registerUseCase: RegisterUseCase,
//    private val loginUseCase: LoginUseCase,
//    private val dataStoreManager: DataStoreManager
//    ) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return RegisterViewModel(application, registerUseCase, loginUseCase, takePhotoUseCase, pickImageUseCase, dataStoreManager) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
