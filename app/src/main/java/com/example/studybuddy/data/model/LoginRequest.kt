package com.example.studybuddy.data.model

// login means username. Everywhere else we will use username, but it will be sent as login here.
data class LoginRequest(val login: String, val password: String)
