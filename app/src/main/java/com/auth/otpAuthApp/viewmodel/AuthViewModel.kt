package com.auth.otpAuthApp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth.otpAuthApp.analytics.AnalyticsLogger
import com.auth.otpAuthApp.data.OtpManager
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AuthViewModel : ViewModel() {

    private val otpManager = OtpManager()
    val state = mutableStateOf(AuthState())


    // 1. Create a StateFlow to hold the raw email input
     val emailInput = MutableStateFlow("")

    init {
        // 2. Observe the email input with a debounce
        observeEmailValidation()
    }

    @OptIn(FlowPreview::class)
    private fun observeEmailValidation() {
        emailInput
            .debounce(1500L) // Wait for 500ms of inactivity
            .onEach { email ->
                if (email.isNotEmpty()) {
                    val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    state.value = state.value.copy(
                        error = if (isValid) null else "Invalid email format"
                    )
                } else {
                    state.value = state.value.copy(error = null)
                }
            }.launchIn(viewModelScope)
    }
    fun validateEmail(email: String) {
        // 3. Update the UI state immediately so the TextField stays responsive
        state.value = state.value.copy(email = email)

        // 4. Push to the stream for debounced validation
        emailInput.value = email
    }

    fun sendOtp(email: String) {
        otpManager.generateOtp(email)
        AnalyticsLogger.otpGenerated()
        state.value = state.value.copy(email = email, screen = Screen.OTP)
    }


    fun verifyOtp(otp: String) {
        val result = otpManager.validateOtp(state.value.email, otp)
        if (result == null) {
            AnalyticsLogger.otpSuccess()
            state.value = state.value.copy(
                screen = Screen.SESSION,
                sessionStart = System.currentTimeMillis()
            )
        } else {
            AnalyticsLogger.otpFailure()
            state.value = state.value.copy(error = result)
        }
    }

    fun logout() {
        AnalyticsLogger.logout()
        state.value = AuthState()
    }
}
