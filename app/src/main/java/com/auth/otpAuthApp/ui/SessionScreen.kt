package com.auth.otpAuthApp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.auth.otpAuthApp.viewmodel.AuthViewModel
import com.auth.otpAuthApp.viewmodel.ProductViewModel
import kotlinx.coroutines.delay

@Composable
fun SessionScreen(
    vm: AuthViewModel = hiltViewModel(),
    sessionExpired: () -> Unit,
) {
    val start =
        vm.state
            .collectAsState()
            .value.sessionStart
    var elapsed by remember { mutableStateOf(0L) }

    LaunchedEffect(start) {
        while (true) {
            delay(1000)
            elapsed = (System.currentTimeMillis() - start) / 1000
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Session Active",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Duration: ${elapsed / 60}:${elapsed % 60}",
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { sessionExpired() }) {
            Text("Logout")
        }
    }
}
