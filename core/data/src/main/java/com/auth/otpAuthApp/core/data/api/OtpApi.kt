package com.auth.otpAuthApp.core.data.api

import retrofit2.http.Body
import retrofit2.http.POST

data class OtpRequest(val email: String)
data class OtpValidationRequest(val email: String, val otp: String)
data class OtpResponse(val message: String, val success: Boolean)

interface OtpApi {
    @POST("/api/otp/generate")
    suspend fun generateOtp(@Body request: OtpRequest): OtpResponse

    @POST("/api/otp/validate")
    suspend fun validateOtp(@Body request: OtpValidationRequest): OtpResponse
}
