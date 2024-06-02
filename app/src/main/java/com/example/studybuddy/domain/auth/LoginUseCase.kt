package com.example.studybuddy.domain.auth

import com.example.studybuddy.data.model.LoginResponse
import com.example.studybuddy.data.repo.AuthRepository
import com.example.studybuddy.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): Flow<Resource<LoginResponse>> = flow{
        emit(Resource.Loading())
        try {
            val response = repository.login(username, password)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}
