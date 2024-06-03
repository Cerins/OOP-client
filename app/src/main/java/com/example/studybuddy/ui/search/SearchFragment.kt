package com.example.studybuddy.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.studybuddy.databinding.FragmentSearch1Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment1 : Fragment() {

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentSearch1Binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearch1Binding.inflate(inflater, container, false)
        return binding.root
    }
}
