package com.example.studybuddy.ui.profile

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
import com.example.studybuddy.R
import com.example.studybuddy.databinding.FragmentProfile1Binding
import com.example.studybuddy.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentProfile1Binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfile1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadUserId()

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.logoutResult.collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            println("Success")
                            runBlocking {
                                viewModel.clearDataStore()
                                findNavController().navigate(R.id.action_navigation_profile1_to_loginFragment)
                            }
                        }

                        is Resource.Error -> {
                            println("Error")
                            // Show error message
                        }

                        is Resource.Loading -> {
                            println("Loading")

                            // Show loading
                        }

                        null -> {}
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userResult.collect { response ->
                    when (response) {
                        is Resource.Success -> {
                            val user = response.data
                            binding.userName.text = user?.firstName
                            binding.description.text = user?.description
                        }
                        is Resource.Error -> {
                            Toast.makeText(requireContext(), "Failed to fetch user", Toast.LENGTH_SHORT).show()
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
}
