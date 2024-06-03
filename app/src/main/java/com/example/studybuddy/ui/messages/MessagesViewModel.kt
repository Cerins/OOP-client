package com.example.studybuddy.ui.messages

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.locale.DataStoreManager
import com.example.studybuddy.data.model.MessageDto
import com.example.studybuddy.data.model.User
import com.example.studybuddy.domain.auth.GetConversationsUseCase
import com.example.studybuddy.domain.auth.GetMessagesUseCase
import com.example.studybuddy.domain.auth.GetUserUseCase
import com.example.studybuddy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.util.Calendar
import java.util.GregorianCalendar
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val messagesUseCase: GetMessagesUseCase,
    private val conversationsUseCase: GetConversationsUseCase,
    private val userUseCase: GetUserUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _messagesResult = MutableStateFlow<Resource<ArrayList<MessageDto>>?>(null)
    val messagesResult: StateFlow<Resource<ArrayList<MessageDto>>?> = _messagesResult

    private val _filteredFriends = MutableStateFlow<Resource<ArrayList<User>>?>(null)
    val filteredFriends: StateFlow<Resource<ArrayList<User>>?> = _filteredFriends

    suspend fun getUserId(): Int? {
        return dataStoreManager.userFlow.first()
    }

    fun loadUserId() {
        viewModelScope.launch {
            val userId = dataStoreManager.userFlow.first()
            userId?.let {
                getConversations(it)
            }
        }
    }

    fun loadMessages(receiverId: Int) {
        viewModelScope.launch {
            val senderId = dataStoreManager.userFlow.first()
            senderId?.let {
                getMessages(it, receiverId)
            }
        }
    }

    private fun getConversations(userId: Int) {
        viewModelScope.launch {
            conversationsUseCase(userId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _filteredFriends.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        result.data?.let { conversationIds ->
                            fetchUsers(conversationIds)
                        }
                    }
                    is Resource.Error -> {
                        _filteredFriends.value = Resource.Error(result.message ?: "Could not retrieve conversations")
                    }
                }
            }
        }
    }

    private suspend fun fetchUsers(ids: ArrayList<Int>) {
        val users = ArrayList<User>()
        for (id in ids) {
            userUseCase(id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { users.add(it) }
                    }
                    is Resource.Error -> {
                        _filteredFriends.value = Resource.Error(result.message ?: "Could not retrieve user with id $id")
                        return@collect
                    }
                    else -> {}
                }
            }
        }
        _filteredFriends.value = Resource.Success(users)
    }

    private fun getMessages(senderId: Int, receiverId: Int) {
        val cal: Calendar = GregorianCalendar()
        cal.add(Calendar.DAY_OF_MONTH, -5)
        viewModelScope.launch {
            messagesUseCase(senderId, receiverId, Timestamp(cal.getTimeInMillis())).collect { result ->
                Log.i("MessagesViewModel", "getMessages result: $result")
                _messagesResult.value = result
            }
        }
    }
}
