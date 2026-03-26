package com.auth.otpAuthApp.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen(
    vm: AuthViewModel,
    onRegisterClick: (String, String) -> Unit,
) {
    val state = vm.state.collectAsState().value
    val email = vm.emailInput.collectAsState("").value
    val password = vm.passwordInput.collectAsState("").value

    // Refactored to call a stateless content Composable to fix Preview issues
    RegisterScreenContent(
        email = email,
        password = password,
        isValidEmail = state.isValidEmail,
        isValidPassword = state.isValidPassword,
        onEmailChange = vm::validateEmail,
        onPasswordChange = vm::validatePassword,
        onRegisterClick = { onRegisterClick(email, password) }
    )
}

@Composable
fun RegisterScreenContent(
    email: String,
    password: String,
    isValidEmail: Boolean,
    isValidPassword: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CustomTextField(
            email = email,
            onValueChange = onEmailChange,
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomPasswordTextField(
            password = password,
            onValueChange = onPasswordChange,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRegisterClick,
            enabled = isValidEmail && isValidPassword,
            modifier =
            Modifier
                .fillMaxWidth()
                .testTag("registerButton"),
        ) {
            Text("Register")
        }

    }
}


@Composable
fun CustomPasswordTextField(
    password: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = password,
        onValueChange = { onValueChange(it) },
        label = { Text("Password") },
        placeholder = { Text("Enter your Password") },
        modifier =
        Modifier
            .fillMaxWidth()
            .testTag("Password"),
    )
}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    // Fixed: Previews cannot instantiate ViewModels directly.
    // Using RegisterScreenContent with mock values instead.
    RegisterScreenContent(
        email = "test@example.com",
        password = "password123",
        isValidEmail = true,
        isValidPassword = true,
        onEmailChange = {},
        onPasswordChange = {},
        onRegisterClick = {}
    )
}
