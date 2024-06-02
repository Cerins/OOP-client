package com.example.studybuddy.data.repo

import com.example.studybuddy.data.model.LoginRequest
import com.example.studybuddy.data.model.LoginResponse
import com.example.studybuddy.data.model.UserRequest
import com.example.studybuddy.data.model.UserResponse
import com.example.studybuddy.data.remote.ApiService
import javax.inject.Inject

class ProfileRepository  @Inject constructor(private val apiService: ApiService) {

    suspend fun getUser(request: UserRequest): UserResponse {
        return apiService.getUser(request)
    }
}