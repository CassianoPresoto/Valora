package org.example.project

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.stringResource
import org.example.project.core.navigation.BottomTab
import org.example.project.features.friends.presentation.FriendsScreen
import org.example.project.features.groups.presentation.GroupsScreen
import org.example.project.features.add.presentation.AddScreen
import org.example.project.features.activities.presentation.ActivitiesScreen
import org.example.project.features.account.presentation.AccountScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        var selectedTab by rememberSaveable { mutableStateOf(BottomTab.Friends) }

        Scaffold(
            bottomBar = {
                NavigationBar {
                    BottomTab.entries.forEach { tab ->
                        NavigationBarItem(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            icon = { Icon(tab.icon, contentDescription = null) },
                            label = { 
                                tab.labelRes?.let { labelRes ->
                                    Text(stringResource(labelRes))
                                }
                            }
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
        BottomTab.Friends -> FriendsScreen.Content(innerPadding)
        BottomTab.Groups -> GroupsScreen.Content(innerPadding)
        BottomTab.Add -> AddScreen.Content(innerPadding)
        BottomTab.Activities -> ActivitiesScreen.Content(innerPadding)
        BottomTab.Account -> AccountScreen.Content(innerPadding)
    }
}