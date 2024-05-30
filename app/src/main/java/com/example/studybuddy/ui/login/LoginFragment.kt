package com.example.studybuddy.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.studybuddy.R
import com.example.studybuddy.databinding.FragmentLoginBinding
import com.example.studybuddy.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            viewModel.login(email, password)
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.loginResult.collect { token ->
                when (token) {
                    is Resource.Success -> {
                        runBlocking {
                            viewModel.saveToken(token.data)
                            findNavController().navigate(R.id.action_loginFragment_to_navigation_profile1)
                        }
                        Log.d("LoginFragment", "Success...")
                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), "An error ocured", Toast.LENGTH_SHORT)
                            .show()
                        Log.d("LoginFragment", "Error...")
                    }

                    is Resource.Loading -> {
                        Log.d("LoginFragment", "Loading...")
                    }

                    null -> {
                        Log.d("LoginFragment", "Null...")
                    }
                }
            }
        }
    }
}
