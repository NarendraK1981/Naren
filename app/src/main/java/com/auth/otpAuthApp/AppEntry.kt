package com.auth.otpAuthApp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.auth.otpAuthApp.viewmodel.AuthViewModel
import com.auth.otpAuthApp.viewmodel.Screen
import com.auth.otpAuthApp.ui.*
import com.auth.otpAuthApp.uiscreens.LoginScreen

@Composable
fun AppEntry() {
    val viewModel = remember { AuthViewModel() }
    val state = viewModel.state.value

    when (state.screen) {
        Screen.LOGIN -> LoginScreen(viewModel)
        Screen.OTP -> OtpScreen(viewModel)
        Screen.SESSION -> SessionScreen(viewModel)
    }
}
