package com.example.studybuddy.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studybuddy.data.model.MessageRequest
import com.example.studybuddy.databinding.FragmentChatBinding
import com.example.studybuddy.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.GregorianCalendar

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private val viewModel: MessagesViewModel by viewModels()
    private lateinit var binding: FragmentChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private var userId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getInt("userId")

        userId?.let { id ->
            viewModel.getUser(id)
            viewModel.loadMessages(id)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val currentUserId = viewModel.getUserId()
            currentUserId?.let { id ->
                setupRecyclerView(id)
                viewModel.messagesResult.collect { response ->
                    when (response) {
                        is Resource.Success -> {
                            val messages = response.data
                            messages?.let {
                                chatAdapter.updateMessages(it, id)
                            }

                            binding.sendButton.setOnClickListener {
                                val message = binding.messageBox.text.toString()
                                val cal: Calendar = GregorianCalendar()

                                if(!message.isNullOrEmpty()) {
                                    val messageObject = MessageRequest(
                                        message,
                                        null,
                                        null,
                                        currentUserId,
                                        userId,
                                        null,
                                    )
                                    viewModel.createMessage(messageObject)
                                    binding.messageBox.setText("")
                                }
                            }
                        }

                        is Resource.Loading -> {
                            // Show loading state if necessary
                        }

                        is Resource.Error -> {
                            // Handle error state if necessary
                        }

                        else -> {}
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userResult.collect { response ->
                when (response) {
                    is Resource.Success -> {
                        binding.recieverName.text = response.data?.firstName
                    }

                    is Resource.Loading -> {
                        // Show loading state if necessary
                    }

                    is Resource.Error -> {
                        // Handle error state if necessary
                    }

                    else -> {}
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.messageResult.collect { response ->
                when (response) {
                    is Resource.Success -> {
                        val messages = response.data
                        messages?.let {
                            chatAdapter.addMessage(it)
                        }
                    }

                    is Resource.Loading -> {
                        // Show loading state if necessary
                    }

                    is Resource.Error -> {
                        // Handle error state if necessary
                    }

                    else -> {}
                }
            }
        }
    }

    private fun setupRecyclerView(id: Int) {
        chatAdapter = ChatAdapter(id, emptyList())
        binding.messagesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }
    }
}