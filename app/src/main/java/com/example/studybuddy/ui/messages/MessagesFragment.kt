package com.example.studybuddy.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.studybuddy.R
import com.example.studybuddy.databinding.FragmentMessages1Binding
import com.example.studybuddy.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessagesFragment1 : Fragment() {

    private val viewModel: MessagesViewModel by viewModels()
    private lateinit var binding: FragmentMessages1Binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessages1Binding.inflate(inflater, container, false)
        return binding.root
    }
}
