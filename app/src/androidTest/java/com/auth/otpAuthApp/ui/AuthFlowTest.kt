package com.auth.otpAuthApp.ui

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
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

        // 2. Enter email
        composeTestRule.onNodeWithTag("emailField").performTextInput("test@example.com")

        // Wait for the Send OTP button to be enabled (due to debounce in ViewModel)
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithTag("sendOtpButton")
                .fetchSemanticsNodes().any { 
                    it.config.getOrNull(SemanticsProperties.Disabled) == null 
                }
        }

        composeTestRule.onNodeWithTag("sendOtpButton").performClick()

        // 3. Check if OTP screen is displayed
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText("Verify OTP").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Verify OTP").assertIsDisplayed()
    }

    @Test
    fun testInvalidEmailError() {
        // 1. Enter invalid email
        composeTestRule.onNodeWithTag("emailField").performTextInput("invalid-email")

        // 2. Wait for error message to appear (due to debounce in ViewModel)
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithTag("errorMessage").fetchSemanticsNodes().isNotEmpty()
        }

        // 3. Check if error message is displayed
        composeTestRule.onNodeWithTag("errorMessage").assertIsDisplayed()
        composeTestRule.onNodeWithText("Invalid email format").assertIsDisplayed()
    }
}
