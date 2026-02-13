package com.auth.otpAuthApp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.auth.otpAuthApp.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SessionScreen(vm: AuthViewModel) {
    val start = vm.state.value.sessionStart
    var elapsed by remember { mutableStateOf(0L) }

    LaunchedEffect(start) {
        while (true) {
            delay(1000)
            elapsed = (System.currentTimeMillis() - start) / 1000
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Session Active",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Duration: ${elapsed / 60}:${elapsed % 60}"
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { vm.logout() }) {
            Text("Logout")
        }
    }
}
