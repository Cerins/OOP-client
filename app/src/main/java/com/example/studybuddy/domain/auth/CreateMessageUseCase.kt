package com.example.studybuddy.domain.auth

import com.example.studybuddy.data.model.MessageDto
import com.example.studybuddy.data.model.MessageRequest
import com.example.studybuddy.data.repo.MessageRepository
import com.example.studybuddy.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateMessageUseCase @Inject constructor(private val repository: MessageRepository) {

    suspend operator fun invoke(request: MessageRequest): Flow<Resource<MessageDto>> = flow {
        emit(Resource.Loading())
        try {
            val response = repository.createMessage(request)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Could not send message"))
        }
    }
}
