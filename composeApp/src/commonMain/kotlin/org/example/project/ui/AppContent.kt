package org.example.project.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import org.example.project.core.navigation.BottomTab
import org.example.project.features.account.presentation.AccountScreen
import org.example.project.features.activities.presentation.ActivitiesScreen
import org.example.project.features.add.presentation.AddScreen
import org.example.project.features.friends.presentation.FriendsScreen
import org.example.project.features.groups.presentation.GroupsScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AppContent(tab: BottomTab, innerPadding: PaddingValues) {
    when (tab) {
        BottomTab.Friends -> FriendsScreen.Content(innerPadding)
        BottomTab.Groups -> GroupsScreen.Content(innerPadding)
        BottomTab.Add -> AddScreen.Content(innerPadding)
        BottomTab.Activities -> ActivitiesScreen.Content(innerPadding)
        BottomTab.Account -> AccountScreen.Content(innerPadding)
    }
}
