package com.auth.otpAuthApp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.auth.otpAuthApp.ui.OtpScreen
import com.auth.otpAuthApp.ui.SessionScreen
import com.auth.otpAuthApp.uiscreens.LoginScreen
import com.auth.otpAuthApp.viewmodel.AuthAction
import com.auth.otpAuthApp.viewmodel.AuthEvent
import com.auth.otpAuthApp.viewmodel.AuthViewModel
import com.auth.otpAuthApp.viewmodel.Screen
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun AppEntry(
    modifier: Modifier,
    navController: NavController,
) {
    val viewModel = remember { AuthViewModel() }

    val navHostController = navController as NavHostController

    val onLoginClick: (String) -> Unit = { email ->
        viewModel.authActions.trySend(AuthAction.Login(email))
    }

    val validateOtp: (String) -> Unit = { otp ->
        viewModel.authActions.trySend(AuthAction.Otp(otp))
    }

    val sessionExpired: () -> Unit = {
        navHostController.navigate(Screen.LOGIN.name)
    }

    LaunchedEffect(Unit) {
        viewModel.authEvent
            .onEach { event ->
                when (event) {
                    is AuthEvent.Login -> {
                        viewModel.sendOtp(event.email)
                    }

                    is AuthEvent.Otp -> {
                        viewModel.verifyOtp(event.otp)
                    }

                    is AuthEvent.LoadOtpScreen -> {
                        navHostController.navigate(Screen.OTP.name)
                    }

                    is AuthEvent.LoginSuccess -> {
                        navHostController.navigate(Screen.SESSION.name)
                    }

                    is AuthEvent.LogOut -> {
                        navHostController.navigate(Screen.LOGIN.name)
                    }
                }
            }.collect()
    }

    NavHost(navController = navHostController, startDestination = Screen.LOGIN.name, modifier = modifier) {
        composable(Screen.LOGIN.name) {
            LoginScreen(viewModel, onLoginClick)
        }
        composable(Screen.OTP.name) {
            OtpScreen(viewModel, validateOtp)
        }
        composable(Screen.SESSION.name) {
            SessionScreen(viewModel, sessionExpired)
        }
    }
}
