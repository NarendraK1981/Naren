package com.auth.otpAuthApp.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.auth.otpAuthApp.core.data.api.OtpApi
import com.auth.otpAuthApp.core.data.api.OtpRequest
import com.auth.otpAuthApp.core.data.api.OtpResponse
import com.auth.otpAuthApp.feature.auth.AuthViewModel
import com.auth.otpAuthApp.feature.auth.Screen
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@Config(sdk = [33])
class AuthViewModelTest {
    private lateinit var viewModel: AuthViewModel
    private lateinit var otpApi: OtpApi
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        otpApi = mockk(relaxed = true)
        viewModel = AuthViewModel(otpApi)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be LOGIN`() {
        assertEquals(Screen.Login, viewModel.state.value.screen)
        assertEquals("", viewModel.state.value.email)
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `validateEmail should update email in state immediately`() {
        viewModel.validateEmail("test@example.com")
        assertEquals("test@example.com", viewModel.state.value.email)
    }

    @Test
    fun `invalid email should show error after debounce`() = runTest {
        viewModel.validateEmail("invalid-email")

        // Advance time to trigger debounce
        testScheduler.advanceTimeBy(1000)

        assertEquals("Invalid email format", viewModel.state.value.error)
    }

    @Test
    fun `valid email should not show error after debounce`() = runTest {
        viewModel.validateEmail("valid@example.com")

        testScheduler.advanceTimeBy(1000)

        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `sendOtp should move to OTP screen on success`() = runTest {
        val email = "test@example.com"
        coEvery { otpApi.generateOtp(OtpRequest(email)) } returns OtpResponse("OTP: 123456", true)

        viewModel.sendOtp(email)
        advanceUntilIdle()

        assertEquals(Screen.Otp, viewModel.state.value.screen)
        assertEquals(email, viewModel.state.value.email)
        assertEquals("123456", viewModel.state.value.prepopulatedOtp)
    }

    @Test
    fun `logout should reset state to LOGIN`() = runTest {
        val email = "test@example.com"
        coEvery { otpApi.generateOtp(OtpRequest(email)) } returns OtpResponse("OTP: 123456", true)

        viewModel.sendOtp(email)
        advanceUntilIdle()
        assertEquals(Screen.Otp, viewModel.state.value.screen)

        viewModel.logout()
        assertEquals(Screen.Login, viewModel.state.value.screen)
        assertEquals("", viewModel.state.value.email)
    }
}
