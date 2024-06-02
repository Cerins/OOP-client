package com.example.studybuddy.domain.auth

import com.example.studybuddy.data.model.UserRequest
import com.example.studybuddy.data.model.UserResponse
import com.example.studybuddy.data.repo.ProfileRepository
import com.example.studybuddy.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val repository: ProfileRepository) {
    suspend operator fun invoke(request: UserRequest): Flow<Resource<UserResponse>> = flow{
        emit(Resource.Loading())
        try {
            val response = repository.getUser(request)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}
