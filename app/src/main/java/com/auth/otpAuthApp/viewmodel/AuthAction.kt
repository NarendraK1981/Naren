package com.auth.otpAuthApp.viewmodel

sealed class AuthAction {
    data class Login(
        val email: String,
    ) : AuthAction()

    data class Otp(
        val otp: String,
    ) : AuthAction()
}
