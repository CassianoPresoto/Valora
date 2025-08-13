package org.example.project

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.example.project.core.navigation.BottomTab
import org.example.project.features.explore.presentation.ExploreScreen
import org.example.project.features.home.presentation.HomeScreen
import org.example.project.features.settings.presentation.SettingsScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        var selectedTab by rememberSaveable { mutableStateOf(BottomTab.Home) }

        Scaffold(
            bottomBar = {
                NavigationBar {
                    BottomTab.entries.forEach { tab ->
                        NavigationBarItem(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            icon = { Text(tab.iconEmoji) },
                            label = { Text(tab.label) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            AppContent(selectedTab, innerPadding)
        }
    }
}

@Composable
private fun AppContent(tab: BottomTab, innerPadding: PaddingValues) {
    when (tab) {
        BottomTab.Home -> HomeScreen.Content(innerPadding)
        BottomTab.Explore -> ExploreScreen.Content(innerPadding)
        BottomTab.Settings -> SettingsScreen.Content(innerPadding)
    }
}