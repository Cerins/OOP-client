package com.example.studybuddy.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studybuddy.databinding.FragmentMessagesBinding
import com.example.studybuddy.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MessagesFragment : Fragment() {

    private val viewModel: MessagesViewModel by viewModels()
    private lateinit var binding: FragmentMessagesBinding
    private lateinit var messagesAdapter: MessagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filteredFriends.collect { response ->
                    when (response) {
                        is Resource.Loading -> {
                            // Show loading state if necessary
                        }

                        is Resource.Success -> {
                            val conversations = response.data
                            conversations?.let {
                                messagesAdapter.updateUsers(it)
                            }
                        }

                        is Resource.Error -> {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }

                        else -> {}
                    }
                }
            }
        }

        viewModel.loadConversations()
    }

    // Sets everything up for the RecyclerView in the fragment_messages.xml
    // It uses the MessagesAdapter for that
    private fun setupRecyclerView() {
        messagesAdapter = MessagesAdapter(emptyList()) { user ->
            val action = MessagesFragmentDirections.actionMessagesFragmentToChatFragment(user.id)
            findNavController().navigate(action)
        }
        binding.conversationsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = messagesAdapter
        }
    }
}
