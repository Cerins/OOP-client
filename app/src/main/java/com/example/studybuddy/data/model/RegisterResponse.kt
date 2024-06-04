package com.example.studybuddy.data.model

data class RegisterResponse(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val login: String,
    val description: String?,
    val avatarID: Int?,
    val tags: List<TagDto?>,
    val role: String,
    val subject: String?
)
