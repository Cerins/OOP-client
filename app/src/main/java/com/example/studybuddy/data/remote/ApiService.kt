package com.example.studybuddy.data.remote

import com.example.studybuddy.data.model.LoginRequest
import com.example.studybuddy.data.model.LoginResponse
import com.example.studybuddy.data.model.RegisterRequest
import com.example.studybuddy.data.model.RegisterResponse
import com.example.studybuddy.data.model.UserRequest
import com.example.studybuddy.data.model.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("/auth/logout")
    suspend fun logout()

    @POST("/auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @GET("/user/1")
    suspend fun getUser(@Body request: UserRequest): UserResponse
}
