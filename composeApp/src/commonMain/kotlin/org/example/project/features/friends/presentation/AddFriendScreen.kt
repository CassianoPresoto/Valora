package org.example.project.features.friends.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.features.friends.presentation.components.UserSearchCard
import org.example.project.features.friends.presentation.components.ContactCard
import org.example.project.core.database.entities.UserEntity
import org.example.project.core.database.entities.ContactEntity
import org.jetbrains.compose.resources.stringResource
import valora.composeapp.generated.resources.Res
import valora.composeapp.generated.resources.*
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: AddFriendViewModel = koinInject()
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(Res.string.cancel_button)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = stringResource(Res.string.add_friend_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        // Search field
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = viewModel::updateSearchQuery,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = { 
                Text(
                    if (uiState.selectedTab == AddFriendTab.CONTACTS) {
                        stringResource(Res.string.search_contacts_placeholder)
                    } else {
                        stringResource(Res.string.search_users_placeholder)
                    }
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(Res.string.search_content_description)
                )
            },
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Tab Row (only show on mobile platforms)
        if (uiState.showContactsTab) {
            TabRow(
                selectedTabIndex = uiState.selectedTab.ordinal,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = uiState.selectedTab == AddFriendTab.CONTACTS,
                    onClick = { viewModel.selectTab(AddFriendTab.CONTACTS) },
                    text = { Text(stringResource(Res.string.contacts_tab)) }
                )
                Tab(
                    selected = uiState.selectedTab == AddFriendTab.USERS,
                    onClick = { viewModel.selectTab(AddFriendTab.USERS) },
                    text = { Text(stringResource(Res.string.users_tab)) }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Content based on selected tab
        when (uiState.selectedTab) {
            AddFriendTab.CONTACTS -> {
                ContactsContent(
                    uiState = uiState,
                    onContactClick = viewModel::onContactClick,
                    onRequestPermission = viewModel::requestContactPermission,
                    modifier = Modifier.weight(1f)
                )
            }
            AddFriendTab.USERS -> {
                UsersContent(
                    uiState = uiState,
                    onUserClick = viewModel::onUserClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ContactsContent(
    uiState: AddFriendUiState,
    onContactClick: (ContactEntity) -> Unit,
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            !uiState.hasContactPermission -> {
                // Permission required
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(Res.string.contact_permissions_required),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(onClick = onRequestPermission) {
                        Text(stringResource(Res.string.grant_permission))
                    }
                }
            }
            
            uiState.isLoadingContacts -> {
                // Loading
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(stringResource(Res.string.loading_contacts))
                }
            }
            
            uiState.contacts.isEmpty() -> {
                // No contacts
                Text(
                    text = stringResource(Res.string.no_contacts_found),
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            else -> {
                // Contacts list
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.contacts) { contact ->
                        ContactCard(
                            contact = contact,
                            onClick = { onContactClick(contact) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UsersContent(
    uiState: AddFriendUiState,
    onUserClick: (UserEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoadingUsers -> {
                // Loading
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(stringResource(Res.string.loading_users))
                }
            }
            
            uiState.searchQuery.length < 2 -> {
                // Search hint
                Text(
                    text = "Digite pelo menos 2 caracteres para buscar",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            uiState.users.isEmpty() && uiState.searchQuery.length >= 2 -> {
                // No users found
                Text(
                    text = stringResource(Res.string.no_users_found),
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            else -> {
                // Users list
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.users) { user ->
                        UserSearchCard(
                            user = user,
                            onClick = { onUserClick(user) }
                        )
                    }
                }
            }
        }
    }
}
