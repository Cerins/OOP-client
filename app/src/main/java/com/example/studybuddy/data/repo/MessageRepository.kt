package com.example.studybuddy.data.repo

import com.example.studybuddy.data.model.MessageDto
import com.example.studybuddy.data.remote.ApiService
import java.sql.Timestamp
import javax.inject.Inject

class MessageRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getMessages(senderId: Int, receiverId: Int, dateTimeFrom: Timestamp?): ArrayList<MessageDto> {
        return apiService.getMessages(senderId, receiverId, dateTimeFrom)
    }

    suspend fun getConversations(id: Int): ArrayList<Int> {
        return apiService.getConversations(id)
    }
}