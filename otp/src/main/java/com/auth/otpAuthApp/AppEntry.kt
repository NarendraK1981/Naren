package com.auth.otpAuthApp

import android.net.Uri
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.auth.otpAuthApp.core.domain.model.Product
import com.auth.otpAuthApp.feature.auth.AuthAction
import com.auth.otpAuthApp.feature.auth.AuthEvent
import com.auth.otpAuthApp.feature.auth.AuthViewModel
import com.auth.otpAuthApp.feature.auth.LoginScreen
import com.auth.otpAuthApp.feature.auth.OtpScreen
import com.auth.otpAuthApp.feature.auth.Screen
import com.auth.otpAuthApp.feature.auth.SessionScreen
import com.auth.otpAuthApp.feature.products.ProductListScreen
import com.auth.otpAuthApp.feature.products.ProductPageScreen
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

@Composable
fun AppEntry(
    modifier: Modifier,
    navController: NavController,
) {
    val viewModel: AuthViewModel = hiltViewModel()
    val navHostController = navController as NavHostController

    val onLoginClick: (String) -> Unit = { email ->
        viewModel.authActions.trySend(AuthAction.Login(email))
    }

    val validateOtp: (String) -> Unit = { otp ->
        viewModel.authActions.trySend(AuthAction.Otp(otp))
    }

    val sessionExpired: () -> Unit = {
        navHostController.navigate(Screen.Login) {
            popUpTo(0)
        }
    }

    val productTypeMap = mapOf(typeOf<Product>() to ProductNavType)

    val onProductItemClick: (Product) -> Unit = {
        navHostController.navigate(
            Screen.ProductPage(product = it)
        )
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
                        navHostController.navigate(Screen.Otp)
                    }

                    is AuthEvent.LoginSuccess -> {
                        navHostController.navigate(Screen.Product) {
                            popUpTo(Screen.Login) { inclusive = true }
                        }
                    }

                    is AuthEvent.LogOut -> {
                        navHostController.navigate(Screen.Login) {
                            popUpTo(0)
                        }
                    }
                }
            }.collect()
    }

    NavHost(
        navController = navHostController,
        startDestination = Screen.Login,
        modifier = modifier
    ) {
        composable<Screen.Login> {
            LoginScreen(viewModel, onLoginClick)
        }
        composable<Screen.Otp> {
            OtpScreen(viewModel, validateOtp)
        }
        composable<Screen.Product> {
            ProductListScreen(
                sessionExpired = sessionExpired,
                onProductItemClick  = onProductItemClick
            )
        }
        composable<Screen.ProductPage>(
            typeMap = productTypeMap
        ) { backStackEntry ->
            val productPage: Screen.ProductPage = backStackEntry.toRoute()
            ProductPageScreen(
                sessionExpired = sessionExpired,
                product = productPage.product,
            )
        }
        composable<Screen.Session> {
            SessionScreen(sessionExpired = sessionExpired)
        }
    }
}

val ProductNavType = object : NavType<Product>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Product? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): Product {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: Product) {
        bundle.putString(key, Json.encodeToString(value))
    }

    override fun serializeAsValue(value: Product): String {
        return Uri.encode(Json.encodeToString(value))
    }
}
