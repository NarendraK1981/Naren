package com.auth.otpAuthApp.backend.model

data class RegisterRequest(
    val email: String,
    val password: String,
    val fullName: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null
)

data class User(
    val email: String,
    val passwordHash: String,
    val fullName: String
)
