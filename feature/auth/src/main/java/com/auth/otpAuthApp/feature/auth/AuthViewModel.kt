package com.auth.otpAuthApp.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth.otpAuthApp.core.common.AnalyticsLogger
import com.auth.otpAuthApp.feature.model.OtpManager
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
import kotlinx.serialization.Serializable

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
        _state.value = _state.value.copy(email = email, prepopulatedOtp = otp)
    }

    fun verifyOtp(otp: String) {
        val result = otpManager.validateOtp(_state.value.email, otp)
        if (result == null) {
            AnalyticsLogger.otpSuccess()
            _authEvent.trySend(AuthEvent.LoginSuccess)
            _state.value =
                _state.value.copy(
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
        _state.value = _state.value.copy(isValidEmail = false)
    }
}

sealed class AuthAction {
    data class Login(val email: String) : AuthAction()
    data class Otp(val otp: String) : AuthAction()
}

sealed interface AuthEvent {
    data class Login(val email: String) : AuthEvent
    data class Otp(val otp: String) : AuthEvent
    object LoadOtpScreen : AuthEvent
    object LoginSuccess : AuthEvent
    object LogOut : AuthEvent
}

data class AuthState(
    val email: String = "",
    val error: String? = null,
    val prepopulatedOtp: String? = null,
    val isValidEmail: Boolean = false,
    val sessionStart: Long = 0L,
)

@Serializable
sealed interface Screen {
    @Serializable
    data object Login : Screen
    @Serializable
    data object Otp : Screen
    @Serializable
    data object Product : Screen
    @Serializable
    data class ProductPage(val product: com.auth.otpAuthApp.core.domain.model.Product) : Screen
    @Serializable
    data object Session : Screen
}
