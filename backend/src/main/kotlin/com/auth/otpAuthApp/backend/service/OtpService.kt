package com.auth.otpAuthApp.backend.service

import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

data class OtpData(
    val otp: String,
    val timestamp: Long,
    var attempts: Int = 0
)

@Service
class OtpService {
    private val otpStore = ConcurrentHashMap<String, OtpData>()
    private val expirationTime = 60_000 // 1 minute

    fun generateOtp(email: String): String {
        val otp = (100000..999999).random().toString()
        otpStore[email] = OtpData(otp, System.currentTimeMillis())
        println("Generated OTP for $email: $otp") // For testing purposes
        return otp
    }

    fun validateOtp(email: String, otp: String): String? {
        val data = otpStore[email] ?: return "OTP not generated"

        if (System.currentTimeMillis() - data.timestamp > expirationTime) {
            otpStore.remove(email)
            return "OTP expired"
        }

        if (data.attempts >= 3) {
            otpStore.remove(email)
            return "Maximum attempts exceeded"
        }

        data.attempts++

        return if (data.otp == otp) {
            otpStore.remove(email)
            null
        } else {
            "Invalid OTP"
        }
    }
}
