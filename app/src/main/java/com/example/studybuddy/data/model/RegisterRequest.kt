package com.example.studybuddy.data.model


data class RegisterRequest(
    val description: String?,
    val email: String,
    val firstName: String,
    val lastName: String,
    val login: String,
    val password: String,
    val phone: String,
    val role: String,
    val tags: List<TagDto>?,
    val picture: String?
)
