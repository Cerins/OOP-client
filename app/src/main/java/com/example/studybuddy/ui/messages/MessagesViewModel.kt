package com.example.studybuddy.ui.messages

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.locale.DataStoreManager
import com.example.studybuddy.data.model.MessageDto
import com.example.studybuddy.data.model.MessageRequest
import com.example.studybuddy.data.model.User
import com.example.studybuddy.domain.auth.CreateMessageUseCase
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
    private val messageUseCase: CreateMessageUseCase,
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {

    private val _messagesResult = MutableStateFlow<Resource<ArrayList<MessageDto>>?>(null)
    val messagesResult: StateFlow<Resource<ArrayList<MessageDto>>?> = _messagesResult

    private val _messagesReplyId = MutableStateFlow<Int?>(null)
    val messagesReplyId: StateFlow<Int?> = _messagesReplyId

    private val _filteredFriends = MutableStateFlow<Resource<ArrayList<User>>?>(null)
    val filteredFriends: StateFlow<Resource<ArrayList<User>>?> = _filteredFriends

    private val _userResult = MutableStateFlow<Resource<User>?>(null)
    val userResult: StateFlow<Resource<User>?> = _userResult

    private val _messageResult = MutableStateFlow<Resource<MessageDto>?>(null)
    val messageResult: StateFlow<Resource<MessageDto>?> = _messageResult

    // Gets the current user's Id
    suspend fun getUserId(): Int? {
        return dataStoreManager.userFlow.first()
    }

    // Gets a certain user based on their Id
    fun getUser(id: Int) {
        viewModelScope.launch {
            userUseCase(id).collect { result ->
                _userResult.value = result
            }
        }
    }

    // Saves the current id of the message the user wants to reply to
    fun setReplyMessageId(id: Int?) {
        _messagesReplyId.value = id
    }

    // Sends a message to another user
    fun createMessage(request: MessageRequest) {
        viewModelScope.launch {
            val message = MessageRequest(
                text = request.text,
                respondsToId = _messagesReplyId.value,
                fileName = request.fileName,
                file = request.file,
                senderId = request.senderId,
                receiverId = request.receiverId,
            )
            messageUseCase(message).collect { result ->
                _messageResult.value = result
            }
        }
    }

    // Calls getMessages
    fun loadMessages(receiverId: Int) {
        viewModelScope.launch {
            val senderId = dataStoreManager.userFlow.first()
            senderId?.let {
                getMessages(it, receiverId)
            }
        }
    }

    // Gets all the messages from a certain time (from 5 days ago until today)
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

    // Calls getConversations
    fun loadConversations() {
        viewModelScope.launch {
            val userId = dataStoreManager.userFlow.first()
            userId?.let {
                getConversations(it)
            }
        }
    }

    // Calls fetchUsers and filters out all the users
    // that the current user doesn't have a conversation with
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

    // Gets all of the user's friends
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
}
