package com.auth.otpAuthApp.viewmodel

sealed interface AuthEvent {
    data class Login(
        val email: String,
    ) : AuthEvent

    data class Otp(
        val otp: String,
    ) : AuthEvent

    object LoadOtpScreen : AuthEvent

    object LoginSuccess : AuthEvent

    object LogOut : AuthEvent
}
