package com.auth.otpAuthApp.ui

import android.widget.Toast
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.auth.otpAuthApp.viewmodel.AuthViewModel

@Composable
fun OtpScreen(
    vm: AuthViewModel,
    validateOtp: (String) -> Unit,
) {
    val state = vm.state.collectAsState().value
    var otp by rememberSaveable { mutableStateOf(state.prepopulatedOtp ?: "") }
    val context = LocalContext.current

    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Verify OTP",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = otp,
            onValueChange = { otp = it },
            label = { Text("6-digit OTP") },
            modifier =
            Modifier
                .fillMaxWidth()
                .testTag("otpField"),
        )

        state.error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.testTag("otpErrorMessage"),
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { validateOtp(otp) },
            modifier =
            Modifier
                .fillMaxWidth()
                .testTag("verifyOtpButton"),
        ) {
            Text("Verify")
        }

        state.prepopulatedOtp?.let {
            Toast.makeText(context, "OTP prepulated", Toast.LENGTH_SHORT).show()
        }
    }
}
