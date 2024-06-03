package com.example.studybuddy.domain.auth

import com.example.studybuddy.data.repo.MessageRepository
import com.example.studybuddy.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetConversationsUseCase @Inject constructor(private val repository: MessageRepository) {
    suspend operator fun invoke(id: Int): Flow<Resource<ArrayList<Int>>> = flow{
        emit(Resource.Loading())
        try {
            val response = repository.getConversations(id)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Could not get user ids"))
        }
    }
}