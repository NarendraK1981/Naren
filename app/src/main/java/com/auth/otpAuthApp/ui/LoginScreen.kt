package com.auth.otpAuthApp.uiscreens

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.auth.otpAuthApp.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    vm: AuthViewModel,
    onLoginClick: (String) -> Unit,
) {
    val state = vm.state.collectAsState().value
    val email = vm.emailInput.collectAsState().value

    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Login",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            email = email,
            onValueChange = vm::validateEmail,
        )

        state.error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = it,
                color = androidx.compose.ui.graphics.Color.Red,
                modifier = Modifier.testTag("errorMessage"),
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onLoginClick(email) },
            enabled = state.isValidEmail,
            modifier =
            Modifier
                .fillMaxWidth()
                .testTag("sendOtpButton"),
        ) {
            Text("Send OTP")
        }
    }
}

@Composable
fun CustomTextField(
    email: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = email,
        onValueChange = { onValueChange(it) },
        label = { Text("Email") },
        placeholder = { Text("Enter your email") },
        modifier =
        Modifier
            .fillMaxWidth()
            .testTag("emailField"),
    )
}
