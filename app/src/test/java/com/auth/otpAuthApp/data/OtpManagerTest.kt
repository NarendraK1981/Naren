package com.auth.otpAuthApp.data

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class OtpManagerTest {

    private lateinit var otpManager: OtpManager
    private val email = "test@example.com"

    @Before
    fun setUp() {
        otpManager = OtpManager()
    }

    @Test
    fun `generateOtp should create a 6-digit OTP`() {
        val otp = otpManager.generateOtp(email)
        assertEquals(6, otp.length)
        assertTrue(otp.all { it.isDigit() })
    }

    @Test
    fun `validateOtp with correct OTP should return null`() {
        val otp = otpManager.generateOtp(email)
        val result = otpManager.validateOtp(email, otp)
        assertNull(result)
    }

    @Test
    fun `validateOtp with incorrect OTP should return error message`() {
        otpManager.generateOtp(email)
        val result = otpManager.validateOtp(email, "000000")
        assertEquals("Invalid OTP", result)
    }

    @Test
    fun `validateOtp with no generated OTP should return error message`() {
        val result = otpManager.validateOtp(email, "123456")
        assertEquals("OTP not generated", result)
    }

    @Test
    fun `validateOtp after 3 failed attempts should return error message`() {
        otpManager.generateOtp(email)
        otpManager.validateOtp(email, "111111")
        otpManager.validateOtp(email, "222222")
        otpManager.validateOtp(email, "333333")
        val result = otpManager.validateOtp(email, "444444")
        assertEquals("Maximum attempts exceeded", result)
    }
}
