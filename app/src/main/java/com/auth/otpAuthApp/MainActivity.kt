package com.auth.otpAuthApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(Timber.DebugTree())
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold { innerPadding ->
                    val navController = rememberNavController()
                    AppEntry(modifier = Modifier.padding(innerPadding), navController = navController)
                }
            }
        }
    }
}
