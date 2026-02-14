package com.auth.otpAuthApp.viewmodel

data class AuthState(
    val email: String = "",
    val screen: Screen = Screen.LOGIN,
    val error: String? = null,
    val prepopulatedOtp: String? = null,
    val isValidEmail: Boolean = false,
    val sessionStart: Long = 0L,
)

enum class Screen {
    LOGIN,
    OTP,
    PRODUCT,
    SESSION,
}
