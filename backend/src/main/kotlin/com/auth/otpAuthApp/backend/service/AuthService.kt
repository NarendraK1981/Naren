package com.auth.otpAuthApp.backend.service

import com.auth.otpAuthApp.backend.model.LoginRequest
import com.auth.otpAuthApp.backend.model.RegisterRequest
import com.auth.otpAuthApp.backend.model.User
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class AuthService {
    // A simple in-memory user store for now
    private val users = ConcurrentHashMap<String, User>()

    fun register(request: RegisterRequest): String? {
        if (users.containsKey(request.email)) {
            return "Email already exists"
        }
        
        // In a real app, always hash passwords!
        val newUser = User(
            email = request.email,
            passwordHash = request.password, // This should be BCrypt.hash(password)
            fullName = request.fullName
        )
        
        users[request.email] = newUser
        println("User registered: ${request.email}")
        return null // Success
    }

    fun login(request: LoginRequest): String? {
        val user = users[request.email]
        
        if (user == null) {
            return "User not found"
        }
        
        if (user.passwordHash != request.password) {
            return "Invalid password"
        }
        
        println("User logged in: ${request.email}")
        return null // Success
    }
}
