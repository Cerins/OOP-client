package com.example.studybuddy.data.model

data class RegisterRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val description: String?,
    val phone: String,
    val role: String
)
