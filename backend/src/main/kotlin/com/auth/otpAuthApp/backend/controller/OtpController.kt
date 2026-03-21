package com.auth.otpAuthApp.backend.controller

import com.auth.otpAuthApp.backend.model.*
import com.auth.otpAuthApp.backend.service.OtpService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/otp")
class OtpController(private val otpService: OtpService) {

    @PostMapping("/generate")
    fun generateOtp(@RequestBody request: OtpRequest): OtpResponse {
        val otp = otpService.generateOtp(request.email)
        return OtpResponse("OTP generated successfully: $otp", true)
    }

    @PostMapping("/validate")
    fun validateOtp(@RequestBody request: OtpValidationRequest): OtpResponse {
        val error = otpService.validateOtp(request.email, request.otp)
        return if (error == null) {
            OtpResponse("OTP validated successfully", true)
        } else {
            OtpResponse(error, false)
        }
    }
}
