package com.example.studybuddy.ui.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.locale.DataStoreManager
import com.example.studybuddy.data.model.User
import com.example.studybuddy.domain.auth.GetFriendsUseCase
import com.example.studybuddy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
private val friendsUseCase: GetFriendsUseCase,
private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _friendsResult = MutableStateFlow<Resource<ArrayList<User>>?>(null)
    val friendsResult: StateFlow<Resource<ArrayList<User>>?> = _friendsResult

    fun loadUserId() {
        viewModelScope.launch {
            val userId = dataStoreManager.userFlow.first()
            userId?.let {
                getFriends(it)
            }
        }
    }

    private fun getFriends(id: Int) {
        viewModelScope.launch {
            friendsUseCase(id).collect { result ->
                _friendsResult.value = result
            }
        }
    }
}