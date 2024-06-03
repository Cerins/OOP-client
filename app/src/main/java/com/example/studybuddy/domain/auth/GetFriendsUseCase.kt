package com.example.studybuddy.domain.auth

import com.example.studybuddy.data.model.User
import com.example.studybuddy.data.repo.UserRepository
import com.example.studybuddy.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFriendsUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(id: Int): Flow<Resource<ArrayList<User>>> = flow{
        emit(Resource.Loading())
        try {
            val response = repository.getFriends(id)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Could not get friends"))
        }
    }
}