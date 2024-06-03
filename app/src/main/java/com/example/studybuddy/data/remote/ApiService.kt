package com.example.studybuddy.data.remote

import com.example.studybuddy.data.model.LoginRequest
import com.example.studybuddy.data.model.LoginResponse
import com.example.studybuddy.data.model.MessageDto
import com.example.studybuddy.data.model.RegisterRequest
import com.example.studybuddy.data.model.RegisterResponse
import com.example.studybuddy.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.sql.Timestamp

interface ApiService {
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("/auth/logout")
    suspend fun logout()

    @POST("/auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @GET("/users/{id}/friendships")
    suspend fun getFriends(@Path("id") id: Int): ArrayList<User>

    @GET("/users/{id}")
    suspend fun getUser(@Path("id") id: Int): User

    @GET("/messages/{senderId}/{receiverId}")
    suspend fun getMessages(@Path("senderId") senderId: Int, @Path("receiverId") receiverId: Int, @Query("dateTimeFrom") dateTimeFrom: Timestamp?): ArrayList<MessageDto>

    @GET("/users/{id}/conversations")
    suspend fun getConversations(@Path("id") id: Int): ArrayList<Int>
}
