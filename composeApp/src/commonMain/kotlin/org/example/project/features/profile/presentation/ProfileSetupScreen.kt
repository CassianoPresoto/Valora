package org.example.project.features.profile.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import valora.composeapp.generated.resources.*

/**
 * Screen for setting up or editing user profile
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    viewModel: ProfileSetupViewModel,
    onNavigateBack: () -> Unit,
    onProfileComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(uiState.isProfileComplete) {
        if (uiState.isProfileComplete) {
            onProfileComplete()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        if (uiState.isEditMode) {
                            stringResource(Res.string.edit_profile)
                        } else {
                            stringResource(Res.string.complete_profile)
                        }
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(Res.string.back)
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        ProfileSetupContent(
            uiState = uiState,
            onNameChange = viewModel::updateName,
            onPhoneChange = viewModel::updatePhone,
            onUsernameChange = viewModel::updateUsername,
            onSaveProfile = viewModel::saveProfile,
            onCheckUsernameAvailability = viewModel::checkUsernameAvailability,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@Composable
private fun ProfileSetupContent(
    uiState: ProfileSetupUiState,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onSaveProfile: () -> Unit,
    onCheckUsernameAvailability: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Profile Header
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = if (uiState.isEditMode) {
                        stringResource(Res.string.edit_your_profile)
                    } else {
                        stringResource(Res.string.complete_your_profile)
                    },
                    style = MaterialTheme.typography.titleMedium
                )
                
                Text(
                    text = uiState.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        // Name Field
        OutlinedTextField(
            value = uiState.name,
            onValueChange = onNameChange,
            label = { Text(stringResource(Res.string.full_name)) },
            placeholder = { Text(stringResource(Res.string.enter_your_name)) },
            isError = uiState.nameError != null,
            supportingText = uiState.nameError?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        // Phone Field
        OutlinedTextField(
            value = uiState.phoneNumber,
            onValueChange = onPhoneChange,
            label = { Text(stringResource(Res.string.phone_number)) },
            placeholder = { Text(stringResource(Res.string.phone_placeholder)) },
            isError = uiState.phoneError != null,
            supportingText = uiState.phoneError?.let { { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        // Username Field
        OutlinedTextField(
            value = uiState.username,
            onValueChange = { username ->
                onUsernameChange(username)
                if (username.length >= 3) {
                    onCheckUsernameAvailability(username)
                }
            },
            label = { Text(stringResource(Res.string.username)) },
            placeholder = { Text(stringResource(Res.string.username_placeholder)) },
            isError = uiState.usernameError != null,
            supportingText = {
                when {
                    uiState.usernameError != null -> Text(uiState.usernameError)
                    uiState.isCheckingUsername -> Text(stringResource(Res.string.checking_availability))
                    uiState.isUsernameAvailable == true -> Text(
                        stringResource(Res.string.username_available),
                        color = MaterialTheme.colorScheme.primary
                    )
                    uiState.isUsernameAvailable == false -> Text(
                        stringResource(Res.string.username_taken),
                        color = MaterialTheme.colorScheme.error
                    )
                    else -> Text(stringResource(Res.string.username_help))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        // Privacy Settings
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.privacy_settings),
                    style = MaterialTheme.typography.titleSmall
                )
                
                Text(
                    text = stringResource(Res.string.privacy_settings_description),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(Res.string.searchable_by_phone))
                    Switch(
                        checked = uiState.isSearchableByPhone,
                        onCheckedChange = { /* TODO: Implement */ }
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(Res.string.searchable_by_email))
                    Switch(
                        checked = uiState.isSearchableByEmail,
                        onCheckedChange = { /* TODO: Implement */ }
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(Res.string.searchable_by_username))
                    Switch(
                        checked = uiState.isSearchableByUsername,
                        onCheckedChange = { /* TODO: Implement */ }
                    )
                }
            }
        }
        
        // Error Message
        if (uiState.errorMessage != null) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = uiState.errorMessage,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
        
        // Save Button
        Button(
            onClick = onSaveProfile,
            enabled = uiState.canSave && !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            
            Text(
                if (uiState.isEditMode) {
                    stringResource(Res.string.save_changes)
                } else {
                    stringResource(Res.string.complete_profile)
                }
            )
        }
        
        // Skip Button (only for new profiles)
        if (!uiState.isEditMode) {
            TextButton(
                onClick = { /* TODO: Implement skip */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(Res.string.skip_for_now))
            }
        }
    }
}
