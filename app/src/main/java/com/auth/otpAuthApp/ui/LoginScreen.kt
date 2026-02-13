package com.auth.otpAuthApp.uiscreens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.auth.otpAuthApp.viewmodel.AuthViewModel

@Composable
fun LoginScreen(vm: AuthViewModel) {
    val state = vm.state.value
    val email = vm.emailInput.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            email = email,
            onValueChange = vm::validateEmail
        )

        state.error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = it,
                color = androidx.compose.ui.graphics.Color.Red,
                modifier = Modifier.testTag("errorMessage")
            )
        }


        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { vm.sendOtp(email) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("sendOtpButton")
        ) {
            Text("Send OTP")
        }
    }
}

@Composable
fun CustomTextField( email: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = email,
        onValueChange = { onValueChange(it) },
        label = { Text("Email") },
        modifier = Modifier
            .fillMaxWidth()
            .testTag("emailField")
    )
}
