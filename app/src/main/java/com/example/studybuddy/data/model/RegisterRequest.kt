package com.example.studybuddy.data.model


data class RegisterRequest(
    val email: String,
    val password: String,
    val login: String,
    val firstName: String,
    val lastName: String,
    val description: String?,
    val phone: String,
    val role: String,
    val tags: List<TagRequest?>,
    val picture: String?
)
