package com.auth.otpAuthApp.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
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

        // 2. Enter email using testTag and click Send OTP
        composeTestRule.onNodeWithTag("emailField").performTextInput("test@example.com")
        composeTestRule.onNodeWithTag("sendOtpButton").performClick()

        // 3. Check if OTP screen is displayed
        composeTestRule.onNodeWithText("Verify OTP").assertIsDisplayed()
    }

    @Test
    fun testInvalidEmailError() {
        // 1. Enter invalid email
        composeTestRule.onNodeWithTag("emailField").performTextInput("invalid-email")

        // 2. Wait for debounce (1.5s in ViewModel)
        composeTestRule.mainClock.advanceTimeBy(2000)

        // 3. Check if error message is displayed using testTag
        composeTestRule.onNodeWithTag("errorMessage").assertIsDisplayed()
        composeTestRule.onNodeWithText("Invalid email format").assertIsDisplayed()
    }
}
