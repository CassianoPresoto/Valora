package org.example.project.features.add.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.features.add.presentation.components.AddExpenseForm
import org.example.project.features.add.presentation.components.GroupCard
import org.example.project.features.friends.domain.model.Friend
import org.example.project.features.friends.presentation.components.FriendCard
import org.example.project.features.groups.domain.model.Group
import org.jetbrains.compose.resources.stringResource
import valora.composeapp.generated.resources.Res
import valora.composeapp.generated.resources.add_expense_title
import valora.composeapp.generated.resources.friends_tab
import valora.composeapp.generated.resources.groups_tab
import valora.composeapp.generated.resources.no_friends_available
import valora.composeapp.generated.resources.no_groups_available
import valora.composeapp.generated.resources.select_friend_or_group

object AddExpenseForm {
    @Composable
    fun Content(innerPadding: PaddingValues) {
        val viewModel = remember { AddViewModel() }
        val uiState = viewModel.uiState

        if (uiState.showExpenseForm) {
            AddExpenseForm(
                selectedFriend = uiState.selectedFriend,
                selectedGroup = uiState.selectedGroup,
                expenseName = uiState.expenseName,
                expenseDescription = uiState.expenseDescription,
                expenseAmount = uiState.expenseAmount,
                expenseNameError = uiState.expenseNameError,
                expenseAmountError = uiState.expenseAmountError,
                onExpenseNameChange = viewModel::updateExpenseName,
                onExpenseDescriptionChange = viewModel::updateExpenseDescription,
                onExpenseAmountChange = viewModel::updateExpenseAmount,
                onSubmit = {
                    if (viewModel.validateAndSubmitExpense()) {
                        // Expense was successfully added
                        // TODO: Show success message or navigate back
                    }
                },
                onBack = viewModel::goBackToSelection,
                modifier = Modifier.Companion.padding(innerPadding)
            )
        } else {
            SelectionScreen(
                uiState = uiState,
                onTabSelected = viewModel::selectTab,
                onFriendSelected = viewModel::selectFriend,
                onGroupSelected = viewModel::selectGroup,
                modifier = Modifier.Companion.padding(innerPadding)
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun SelectionScreen(
        uiState: AddUiState,
        onTabSelected: (SelectionTab) -> Unit,
        onFriendSelected: (Friend) -> Unit,
        onGroupSelected: (Group) -> Unit,
        modifier: Modifier = Modifier.Companion
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = stringResource(Res.string.add_expense_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Companion.Bold,
                modifier = Modifier.Companion.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(Res.string.select_friend_or_group),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.Companion.padding(bottom = 24.dp)
            )

            // Tab Row
            TabRow(
                selectedTabIndex = uiState.selectedTab.ordinal,
                modifier = Modifier.Companion.fillMaxWidth()
            ) {
                Tab(
                    selected = uiState.selectedTab == SelectionTab.FRIENDS,
                    onClick = { onTabSelected(SelectionTab.FRIENDS) },
                    text = { Text(stringResource(Res.string.friends_tab)) }
                )
                Tab(
                    selected = uiState.selectedTab == SelectionTab.GROUPS,
                    onClick = { onTabSelected(SelectionTab.GROUPS) },
                    text = { Text(stringResource(Res.string.groups_tab)) }
                )
            }

            Spacer(modifier = Modifier.Companion.height(16.dp))

            // Content based on selected tab
            when (uiState.selectedTab) {
                SelectionTab.FRIENDS -> {
                    if (uiState.friends.isEmpty()) {
                        Box(
                            modifier = Modifier.Companion.fillMaxSize(),
                            contentAlignment = Alignment.Companion.Center
                        ) {
                            Text(
                                text = stringResource(Res.string.no_friends_available),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.friends) { friend ->
                                FriendCard(
                                    friend = friend,
                                    onClick = { onFriendSelected(friend) }
                                )
                            }
                        }
                    }
                }

                SelectionTab.GROUPS -> {
                    if (uiState.groups.isEmpty()) {
                        Box(
                            modifier = Modifier.Companion.fillMaxSize(),
                            contentAlignment = Alignment.Companion.Center
                        ) {
                            Text(
                                text = stringResource(Res.string.no_groups_available),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.groups) { group ->
                                GroupCard(
                                    group = group,
                                    onClick = { onGroupSelected(group) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}