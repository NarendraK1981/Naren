package com.auth.otpAuthApp.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.auth.otpAuthApp.feature.auth.AuthViewModel
import com.auth.otpAuthApp.feature.auth.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
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
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AuthViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be LOGIN`() {
        assertEquals(Screen.LOGIN, viewModel.state.value.screen)
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
        testScheduler.advanceTimeBy(2000)

        assertEquals("Invalid email format", viewModel.state.value.error)
    }

    @Test
    fun `valid email should not show error after debounce`() = runTest {
        viewModel.validateEmail("valid@example.com")

        testScheduler.advanceTimeBy(2000)

        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `sendOtp should move to OTP screen and integrate with OtpManager`() {
        viewModel.sendOtp("test@example.com")
        assertEquals(Screen.OTP, viewModel.state.value.screen)
        assertEquals("test@example.com", viewModel.state.value.email)
    }

    @Test
    fun `logout should reset state to LOGIN`() {
        viewModel.sendOtp("test@example.com")
        viewModel.logout()
        assertEquals(Screen.LOGIN, viewModel.state.value.screen)
        assertEquals("", viewModel.state.value.email)
    }
}
