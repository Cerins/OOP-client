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
    val tags: String?, // Has to be set as Set<TagDto>?, but later,
    val picture: String? // Will need the actual data type. String? is only placeholder
)
