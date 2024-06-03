package com.example.studybuddy.ui.register

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.studybuddy.R
import com.example.studybuddy.data.model.RegisterRequest
import com.example.studybuddy.data.model.TagDto
import com.example.studybuddy.databinding.DialogSelectImageBinding
import com.example.studybuddy.databinding.FragmentRegisterBinding
import com.example.studybuddy.util.Resource
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var binding: FragmentRegisterBinding

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all { it.value }
            if (granted) {
                showImageSourceDialog()
            } else {
                viewModel.requestPermissions(permissions = listOf("android.permission.CAMERA"))
            }
        }

    private val takePhotoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as Bitmap

                binding.ivProfile.setImageBitmap(imageBitmap)
            }
        }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                binding.ivProfile.setImageURI(imageUri)
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter1 = ArrayAdapter(
            requireContext(), R.layout.view_spinner_item, listOf("Student", "Teacher", "Parent")
        )

        val adapter2 = ArrayAdapter(
            requireContext(), R.layout.view_spinner_item, listOf("Rīgas Valsts 1. ģimnāzija", "Rīgas Valsts 2. ģimnāzija", "Rīgas Valsts 3. ģimnāzija")
        )

        val adapter3 = ArrayAdapter(
            requireContext(), R.layout.view_spinner_item, listOf("16-19", "19-23", "23-")
        )

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spType.adapter = adapter1

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spTeaching.adapter = adapter2

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spAge.adapter = adapter3

        binding.btnRegister.setOnClickListener {
            val description = binding.etDesc.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val firstName = binding.etName.text.toString().trim()
            val lastName = binding.etSurname.text.toString().trim()
            val login = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()

            if (!isInputValid(description, email, firstName, lastName, login, password, phone)) {
                return@setOnClickListener
            }

            val role = when (binding.spType.selectedItemPosition) {
                1 -> "teacher"
                2 -> "parent"
                else -> "student"
            }

            val age = when (binding.spAge.selectedItemPosition) {
                1 -> "19-23"
                2 -> "23-"
                else -> "16-19"
            }

            val establishment = when (binding.spTeaching.selectedItemPosition) {
                1 -> "Rīgas Valsts 2. ģimnāzija"
                2 -> "Rīgas Valsts 3. ģimnāzija"
                else -> "Rīgas Valsts 1. ģimnāzija"
            }

            val tags = listOf(
                TagDto(1, age, "age"),
                TagDto(2, establishment, "establishment"),
                TagDto(3, "Matemātika", "interests")
            )

            val image = null // for now

            val passwordError =
                binding.etPassword.text.toString().trim() != binding.etPassword.text.toString()
                    .trim()
            val registerRequest =
                RegisterRequest(
                    description,
                    email,
                    firstName,
                    lastName,
                    login,
                    password,
                    phone,
                    role,
                    tags,
                    null
                )
            if (passwordError.not()) {
                viewModel.register(registerRequest)
            }
        }

        binding.ivProfile.setOnClickListener {
            viewModel.requestPermissions(listOf("android.permission.CAMERA"))
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.permissionRequest.collect { event ->
                    event?.getContentIfNotHandled()?.let { permissions ->
                        requestPermissionLauncher.launch(permissions.toTypedArray())
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.intentFlow.collect { event ->
                    event?.getContentIfNotHandled()?.let { intent ->
                        if (intent.action == MediaStore.ACTION_IMAGE_CAPTURE) {
                            takePhotoLauncher.launch(intent)
                        } else if (intent.action == Intent.ACTION_PICK) {
                            pickImageLauncher.launch(intent)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerResult.collect { register ->
                    when (register) {
                        is Resource.Loading -> {
                            Log.d("RegisterFragment reg", "Loading...")
                        }

                        is Resource.Success -> {
                        //    viewModel.login(
                        //        binding.etEmail.text.trim().toString(),
                        //        binding.etPassword.text.trim().toString()
                        //   )
                            // login istegi ve giris yap
                            Log.d("RegisterFragment reg", "Success...")
                            findNavController().navigate(R.id.action_registerFragment_to_navigation_profile1)
                        }

                        is Resource.Error -> {
                            Toast.makeText(requireContext(), "An error ocured", Toast.LENGTH_SHORT)
                                .show()
                            Log.d("RegisterFragment reg", "Error...")
                        }

                        null -> {}
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginResult.collect { token ->
                    when (token) {
                        is Resource.Loading -> {
                            Log.d("RegisterFragment", "Loading...")
                        }

                        is Resource.Success -> {
                        //    runBlocking {
                        //        viewModel.saveToken(token.data)
                        //    findNavController().navigate(R.id.action_registerFragment_to_navigation_profile1)
                        //    }
                            Log.d("RegisterFragment", "Success...")
                        }

                        is Resource.Error -> {
                            Toast.makeText(requireContext(), "An error ocured", Toast.LENGTH_SHORT)
                                .show()
                            Log.d("RegisterFragment", "Error...")
                        }

                        null -> {}
                    }
                }
            }
        }

        binding.buttonAddInterest.setOnClickListener {
            val newInterest = binding.editTextNewInterest.text.toString()
            viewModel.addInterest(newInterest)
            binding.editTextNewInterest.text.clear()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.interests.collect { interests ->
                    updateChipGroup(interests)
                }
            }
        }


//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.token.collect { token ->
//                    token?.let {
//                        findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
//                    }
//                }
//            }
//        }
    }

    private fun isInputValid(
        description: String,
        email: String,
        firstName: String,
        lastName: String,
        login: String,
        password: String,
        phone: String
    ): Boolean {

        if (firstName.isEmpty()) {
            showToast("First name is required")
            return false
        }

        if (firstName.length < 2) {
            showToast("First name should be at least 2 characters long")
            return false
        }

        if (!isAlpha(firstName)) {
            showToast("First name should not contain numbers or symbols")
            return false
        }

        if (lastName.isEmpty()) {
            showToast("Last name is required")
            return false
        }

        if (lastName.length < 2) {
            showToast("Last name should be at least 2 characters long")
            return false
        }

        if (!isAlpha(lastName)) {
            showToast("Last name should not contain numbers or symbols")
            return false
        }

        if (phone.isEmpty()) {
            showToast("Phone number cannot be empty")
            return false
        }

        if (!isValidPhone(phone)) {
            showToast("Phone number must be exactly 8 digits long")
            return false
        }

        if (email.isEmpty()) {
            showToast("Email is required")
            return false
        }

        if (!isValidEmail(email)) {
            showToast("Invalid email")
            return false
        }

        if (login.isEmpty()) {
            showToast("Username is required")
            return false
        }

        if (login.length < 3) {
            showToast("Username should be at least 3 characters long")
            return false
        }

        if (!isAlphaNumeric(login)) {
            showToast("Username should not contain symbols")
            return false
        }

        if (password.isEmpty()) {
            showToast("Phone number is required")
            return false
        }

        if (password.length < 6) {
            showToast("Password must be at least 6 characters long")
            return false
        }


        return true
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$"
        return email.matches(emailPattern.toRegex())
    }

    private fun isValidPhone(phone: String): Boolean {
        return phone.length == 8 && phone.all { it.isDigit() }
    }

    private fun isAlpha(input: String): Boolean {
        val alphaPattern = "^[A-Za-z]+$"
        return input.matches(alphaPattern.toRegex())
    }

    private fun isAlphaNumeric(input: String): Boolean {
        val alphaNumericPattern = "^[A-Za-z0-9]+$"
        return input.matches(alphaNumericPattern.toRegex())
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun updateChipGroup(interests: List<InterestChip>) {
        binding.chipGroupInterests.removeAllViews()
        for (interest in interests.sortedByDescending { it.isCloseVisible }) {
            val chip = Chip(requireContext()).apply {
                text = interest.name
                setChipStrokeWidthResource(R.dimen.border)
                setChipStrokeColorResource(R.color.orange_100)
                chipStrokeColor = resources.getColorStateList(R.color.orange_100, null)
                chipCornerRadius = 16f
                chipBackgroundColor = resources.getColorStateList(R.color.orange_50, null)
                background = resources.getDrawable(R.drawable.bg_et, null)
                isCloseIconVisible = interest.isCloseVisible
                setOnCloseIconClickListener {
                    viewModel.removeInterest(interest.name)
                }
            }
            binding.chipGroupInterests.addView(chip)
        }
    }

    private fun showImageSourceDialog() {
        val dialogBinding = DialogSelectImageBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext()).setView(dialogBinding.root).create()

        dialogBinding.buttonTakePhoto.setOnClickListener {
            viewModel.onTakePhotoClicked()
            dialog.dismiss()
        }

        dialogBinding.buttonPickGallery.setOnClickListener {
            viewModel.onPickImageClicked()
            dialog.dismiss()
        }

        dialog.show()
    }

}
