package com.example.studybuddy.data.repo

import com.example.studybuddy.data.model.LoginRequest
import com.example.studybuddy.data.model.LoginResponse
import com.example.studybuddy.data.model.RegisterRequest
import com.example.studybuddy.data.model.RegisterResponse
import com.example.studybuddy.data.remote.ApiService
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(LoginRequest(email, password))
    }

    suspend fun logout() {
        apiService.logout()
    }

    suspend fun register(request: RegisterRequest): RegisterResponse {
        return apiService.register(request)
    }
}
