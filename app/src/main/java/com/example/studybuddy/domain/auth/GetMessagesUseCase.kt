package com.example.studybuddy.domain.auth

import android.util.Log
import com.example.studybuddy.data.model.MessageDto

import com.example.studybuddy.data.repo.MessageRepository
import com.example.studybuddy.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.sql.Timestamp
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(private val repository: MessageRepository) {
    suspend operator fun invoke(senderId: Int, receiverId: Int, dateTimeFrom: Timestamp?): Flow<Resource<ArrayList<MessageDto>>> = flow {
        emit(Resource.Loading())
        try {
            val response = repository.getMessages(senderId, receiverId, dateTimeFrom)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            Log.e("GetMessagesUseCase", "Error fetching messages", e)
            emit(Resource.Error(e.localizedMessage ?: "No messages could be received"))
        }
    }
}