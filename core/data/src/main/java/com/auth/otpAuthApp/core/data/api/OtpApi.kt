package com.auth.otpAuthApp.core.data.api

import retrofit2.http.Body
import retrofit2.http.POST

data class OtpRequest(val email: String)
data class OtpValidationRequest(val email: String, val otp: String)
data class OtpResponse(val message: String, val success: Boolean)

data class RegisterUser(val email: String, val password: String)

interface OtpApi {
    @POST("/generate-otp")
    suspend fun generateOtp(@Body request: OtpRequest): OtpResponse

    @POST("/validate-otp")
    suspend fun validateOtp(@Body request: OtpValidationRequest): OtpResponse

    @POST("/register")
    suspend fun registerUser(@Body request: RegisterUser): OtpResponse
}
