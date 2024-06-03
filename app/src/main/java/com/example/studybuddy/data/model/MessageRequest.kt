package com.example.studybuddy.data.model

data class MessageRequest(
    val text: String?,
    val fileName: String?,
    val file: String?,
    val senderId: Int?, // In most cases current logged in user ID
    val receiverId: Int?, // The user you are in a conversation with
    val respondsToId: Int?, // some other or your own message ID
)