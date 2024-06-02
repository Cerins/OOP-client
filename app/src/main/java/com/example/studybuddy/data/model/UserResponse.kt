package com.example.studybuddy.data.model

data class UserResponse (
    val id: Int,
    val firstName: String,
    val lastName: String,
    val description: String?,
    val avatarID: Int?,
    val tags: Set<TagDto>?,
    val role: String,
    val subject: Int?
)
