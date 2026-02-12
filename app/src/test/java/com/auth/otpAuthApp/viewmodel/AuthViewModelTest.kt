package com.auth.otpAuthApp.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private lateinit var viewModel: AuthViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

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
    fun `validateEmail should update email in state`() {
        viewModel.validateEmail("test@example.com")
        assertEquals("test@example.com", viewModel.state.value.email)
    }

    @Test
    fun `sendOtp should move to OTP screen`() {
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
