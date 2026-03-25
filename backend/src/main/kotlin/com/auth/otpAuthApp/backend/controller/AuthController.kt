package com.auth.otpAuthApp.backend.controller

import com.auth.otpAuthApp.backend.model.AuthResponse
import com.auth.otpAuthApp.backend.model.LoginRequest
import com.auth.otpAuthApp.backend.model.RegisterRequest
import com.auth.otpAuthApp.backend.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        val error = authService.register(request)
        return if (error == null) {
            ResponseEntity.ok(AuthResponse(success = true, message = "User registered successfully"))
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AuthResponse(success = false, message = error))
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        val error = authService.login(request)
        return if (error == null) {
            ResponseEntity.ok(AuthResponse(success = true, message = "Login successful", token = "fake-jwt-token"))
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthResponse(success = false, message = error))
        }
    }
}
