package com.nrs.finopaytest

import android.Manifest
import android.os.Build
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.rule.GrantPermissionRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class WeatherUiTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val permissionRule: GrantPermissionRule = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        GrantPermissionRule.grant()
    }

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun addCityAndVerifyDetails() {
        // 1. Wait for the screen to be ready. 
        // We wait for the TopAppBar title first to ensure the Activity is launched and composed.
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule.onAllNodesWithText("City Weather Management").fetchSemanticsNodes().isNotEmpty()
        }

        // 2. Wait for the "Add New City" card to be visible.
        // If initial loading is slow, this might take time.
        composeTestRule.waitUntil(timeoutMillis = 40000) {
            composeTestRule
                .onAllNodesWithText("Add New City")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // 3. Click "Add New City" card
        composeTestRule.onNodeWithText("Add New City").performClick()

        // 4. Fill in the Add City dialog
        // We wait for the dialog title to appear
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Add New City").fetchSemanticsNodes().size > 1
        }
        
        composeTestRule.onNodeWithText("City Name").performTextInput("Chicago")
        composeTestRule.onNodeWithText("Country Code (e.g. NG, GB)").performTextInput("US")
        
        // Click the Add button in the dialog
        composeTestRule.onNodeWithText("Add").performClick()

        // 5. Wait for the city to appear in the list
        composeTestRule.waitUntil(timeoutMillis = 40000) {
            composeTestRule.onAllNodesWithText("Chicago").fetchSemanticsNodes().isNotEmpty()
        }
        
        // 6. Click on the newly added city card
        composeTestRule.onNodeWithText("Chicago").performClick()

        // 7. Verify details in WeatherDetailScreen
        composeTestRule.waitUntil(timeoutMillis = 30000) {
            composeTestRule.onAllNodesWithText("Feels like").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Chicago").assertIsDisplayed()
        composeTestRule.onNodeWithText("Feels like").assertIsDisplayed()
        composeTestRule.onNodeWithText("Humidity").assertIsDisplayed()
        
        // Verify that temperature values are shown
        // We use onAllNodes because both the main temperature and "Feels like" contain "°C"
        composeTestRule.onAllNodes(hasText("°C", substring = true)).onFirst().assertIsDisplayed()
    }
}
