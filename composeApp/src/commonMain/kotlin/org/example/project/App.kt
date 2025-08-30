package org.example.project

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.core.navigation.BottomTab
import org.example.project.ui.AppContent
import org.example.project.ui.components.RailMenuItem
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
                var isRailCollapsed by rememberSaveable { mutableStateOf(false) }
                Row(modifier = Modifier.fillMaxSize()) {
                    NavigationRail(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(
                                animateDpAsState(
                                    targetValue = if (isRailCollapsed) 72.dp else 220.dp,
                                    animationSpec = tween(durationMillis = 200)
                                ).value
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            BottomTab.entries.forEach { tab ->
                                RailMenuItem(
                                    selected = selectedTab == tab,
                                    label = tab.labelRes?.let { stringResource(it) } ?: "",
                                    onClick = { selectedTab = tab },
                                    icon = { Icon(tab.icon, contentDescription = null) },
                                    showLabel = !isRailCollapsed,
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            RailMenuItem(
                                selected = false,
                                label = "",
                                onClick = { isRailCollapsed = !isRailCollapsed },
                                icon = {
                                    Icon(
                                        imageVector = if (isRailCollapsed) Icons.Filled.ChevronRight else Icons.Filled.ChevronLeft,
                                        contentDescription = null
                                    )
                                },
                                showLabel = false,
                            )
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