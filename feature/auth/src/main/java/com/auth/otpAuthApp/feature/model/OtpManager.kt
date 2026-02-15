package com.auth.otpAuthApp.feature.model

import timber.log.Timber

data class OtpData(
    val otp: String,
    val time: Long,
    var attempts: Int,
)

class OtpManager {
    private val otpStore = mutableMapOf<String, OtpData>()

    fun generateOtp(email: String): String {
        val otp = (100000..999999).random().toString()

        otpStore[email] =
            OtpData(
                otp = otp,
                time = System.currentTimeMillis(),
                attempts = 0,
            )

        Timber.d("OTP for %s is %s", email, otp)

        return otp
    }

    fun validateOtp(
        email: String,
        input: String,
    ): String? {
        val data = otpStore[email] ?: return "OTP not generated"

        if (System.currentTimeMillis() - data.time > 60_000) {
            otpStore.remove(email)
            return "OTP expired"
        }

        if (data.attempts >= 3) {
            otpStore.remove(email)
            return "Maximum attempts exceeded"
        }

        data.attempts++

        return if (data.otp == input) {
            otpStore.remove(email)
            null
        } else {
            "Invalid OTP"
        }
    }
}
