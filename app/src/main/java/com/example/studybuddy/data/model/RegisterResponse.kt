package com.example.studybuddy.data.model

data class RegisterResponse(
    val id: Int,
    val avatarId: Int?,
    val firstName: String,
    val lastName: String,
    val description: String?,
    val role: String,
    val subject: String?
)
