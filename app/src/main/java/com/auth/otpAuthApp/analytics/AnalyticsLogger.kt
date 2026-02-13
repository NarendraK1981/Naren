package com.auth.otpAuthApp.analytics



import timber.log.Timber

object AnalyticsLogger {
    fun otpGenerated() = Timber.d("OTP Generated")
    fun otpSuccess() = Timber.d("OTP Validation Success")
    fun otpFailure() = Timber.d("OTP Validation Failure")
    fun logout() = Timber.d("Logout")
}
