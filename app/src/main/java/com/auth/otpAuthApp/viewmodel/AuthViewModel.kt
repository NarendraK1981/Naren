package com.auth.otpAuthApp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth.otpAuthApp.analytics.AnalyticsLogger
import com.auth.otpAuthApp.data.OtpManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {
    private val _authActions = Channel<AuthAction>(Channel.BUFFERED)
    val authActions: Channel<AuthAction> = _authActions

    private val _authEvent = Channel<AuthEvent>(UNLIMITED)
    val authEvent = _authEvent.receiveAsFlow()

    private val otpManager = OtpManager()
    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    private val _emailInput = MutableStateFlow("")
    val emailInput: StateFlow<String> = _emailInput

    init {
        observeEmailValidation()

        _authActions
            .consumeAsFlow()
            .onEach { action ->
                handleAction(action)
            }.launchIn(viewModelScope)
    }

    private fun handleAction(action: AuthAction) {
        when (action) {
            is AuthAction.Login -> {
                _authEvent.trySend(AuthEvent.Login(action.email))
            }
            is AuthAction.Otp -> {
                _authEvent.trySend(AuthEvent.Otp(action.otp))
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeEmailValidation() {
        _emailInput
            .debounce(300L)
            .onEach { email ->
                if (email.isNotEmpty()) {
                    val isValid =
                        android.util.Patterns.EMAIL_ADDRESS
                            .matcher(email)
                            .matches()
                    _state.value =
                        _state.value.copy(
                            error = if (isValid) null else "Invalid email format",
                        )
                    _state.value = _state.value.copy(isValidEmail = isValid)
                } else {
                    _state.value = _state.value.copy(error = null)
                }
            }.launchIn(viewModelScope)
    }

    fun validateEmail(email: String) {
        _state.value = _state.value.copy(email = email)
        _emailInput.value = email
    }

    fun sendOtp(email: String) {
        val otp = otpManager.generateOtp(email)
        AnalyticsLogger.otpGenerated()
        _authEvent.trySend(AuthEvent.LoadOtpScreen)
        _state.value = _state.value.copy(email = email, screen = Screen.OTP, prepopulatedOtp = otp)
    }

    fun verifyOtp(otp: String) {
        val result = otpManager.validateOtp(_state.value.email, otp)
        if (result == null) {
            AnalyticsLogger.otpSuccess()
            _authEvent.trySend(AuthEvent.LoginSuccess)
            _state.value =
                _state.value.copy(
                    screen = Screen.SESSION,
                    sessionStart = System.currentTimeMillis(),
                )
        } else {
            AnalyticsLogger.otpFailure()
            _state.value = _state.value.copy(error = result)
        }
    }

    fun logout() {
        AnalyticsLogger.logout()
        _state.value = AuthState()
        _state.value = _state.value.copy(isValidEmail = false, screen = Screen.LOGIN)
    }
}
