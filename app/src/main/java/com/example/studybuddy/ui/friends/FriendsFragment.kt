package com.example.studybuddy.ui.friends

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studybuddy.databinding.FragmentFriendsBinding
import com.example.studybuddy.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FriendsFragment : Fragment() {

    private val viewModel: FriendsViewModel by viewModels()
    private lateinit var binding: FragmentFriendsBinding
    private lateinit var friendsAdapter: FriendsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.loadUserId()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.friendsResult.collect { response ->
                    when (response) {
                        is Resource.Success -> {
                            val friends = response.data
                            friends?.let {
                                friendsAdapter.updateUsers(it)
                            }
                        }
                        is Resource.Error -> {
                            Toast.makeText(requireContext(), "Failed to fetch friends", Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Loading -> {
                            // Handle loading state if needed
                        }
                        null -> {
                            // Handle null state if needed
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        friendsAdapter = FriendsAdapter(emptyList())
        binding.userList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = friendsAdapter
        }
    }
}
