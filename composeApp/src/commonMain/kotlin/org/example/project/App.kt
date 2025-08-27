package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.core.navigation.BottomTab
import org.example.project.features.account.presentation.AccountScreen
import org.example.project.features.activities.presentation.ActivitiesScreen
import org.example.project.features.add.presentation.AddScreen
import org.example.project.features.friends.presentation.FriendsScreen
import org.example.project.features.groups.presentation.GroupsScreen
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var selectedTab by rememberSaveable { mutableStateOf(BottomTab.Friends) }
        BoxWithConstraints {
            val useNavigationRail = maxWidth >= 600.dp
            if (useNavigationRail) {
                Row(modifier = Modifier.fillMaxSize()) {
                    NavigationRail(
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            BottomTab.entries.forEach { tab ->
                                NavigationRailItem(
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
                    AppContent(selectedTab, PaddingValues(0.dp))
                }
            } else {
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