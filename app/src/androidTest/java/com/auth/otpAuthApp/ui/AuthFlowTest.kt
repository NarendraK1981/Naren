package com.auth.otpAuthApp.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.auth.otpAuthApp.MainActivity
import org.junit.Rule
import org.junit.Test

class AuthFlowTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testFullAuthFlow() {
        // 1. Check if Login screen is displayed
        composeTestRule.onNodeWithText("Login").assertIsDisplayed()

        // 2. Enter email and click Send OTP
        composeTestRule.onNodeWithText("Email").performTextInput("test@example.com")
        composeTestRule.onNodeWithText("Send OTP").performClick()

        // 3. Check if OTP screen is displayed
        composeTestRule.onNodeWithText("Verify OTP").assertIsDisplayed()

        // Note: In a real integration test, we might need a way to get the OTP 
        // from the manager or mock it. For this UI test, we just verify navigation.
    }

    @Test
    fun testInvalidEmailError() {
        // 1. Enter invalid email
        composeTestRule.onNodeWithText("Email").performTextInput("invalid-email")
        
        // 2. Wait for debounce (1.5s in ViewModel)
        composeTestRule.mainClock.advanceTimeBy(2000)

        // 3. Check if error message is displayed
        composeTestRule.onNodeWithText("Invalid email format").assertIsDisplayed()
    }
}
