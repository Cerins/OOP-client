package com.example.studybuddy.data.model

data class MessageDto(
    val id: Int, // This is the message ID
    val text: String?,
    val time: String?,
    val hasFiles: Boolean?,
    val senderId: Int?, // In most cases current logged in user ID
    val receiverId: Int?, // The user you are in a conversation with
    val respondsToId: Int?, // some other or your own message ID
)