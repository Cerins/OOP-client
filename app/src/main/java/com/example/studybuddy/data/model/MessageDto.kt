package com.example.studybuddy.data.model

import java.sql.Timestamp

data class MessageDto(
    val id: Int, // This is the message ID
    val text: String?,
    val time: Timestamp?,
    val senderId: Int?, // In most cases current logged in user ID
    val receiverId: Int?, // The user you are in a conversation with
    val respondsToId: Int?, // some other or your own message ID
    val hasFiles: Boolean,
)