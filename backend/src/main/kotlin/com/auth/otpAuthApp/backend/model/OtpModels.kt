package com.auth.otpAuthApp.backend.model

data class OtpRequest(val email: String)
data class OtpValidationRequest(val email: String, val otp: String)
data class OtpResponse(val message: String, val success: Boolean)
