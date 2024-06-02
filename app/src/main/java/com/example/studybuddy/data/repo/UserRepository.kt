package com.example.studybuddy.data.repo

import com.example.studybuddy.data.model.User
import com.example.studybuddy.data.remote.ApiService
import javax.inject.Inject

class UserRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getFriends(id: Int): ArrayList<User> {
        return apiService.getFriends(id)
    }

    suspend fun getUser(id: Int): User {
        return apiService.getUser(id)
    }
}
