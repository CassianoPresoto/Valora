package org.example.project.features.friends.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.features.friends.presentation.components.FriendCard
import org.example.project.features.friends.domain.model.Friend
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import valora.composeapp.generated.resources.Res
import valora.composeapp.generated.resources.*

object FriendsScreen {
    @Composable
    @Preview
    fun Content(innerPadding: PaddingValues) {
        var searchQuery by remember { mutableStateOf("") }
        var totalDebt by remember { mutableStateOf(-150.50) } // Example: you owe R$ 150.50
        
        // Sample friends data (using domain model)
        val friends = remember {
            listOf(
                Friend(userId = "1", name = "João Silva", totalBalance = 25.50),
                Friend(userId = "2", name = "Maria Santos", totalBalance = -75.00),
                Friend(userId = "3", name = "Pedro Costa", totalBalance = 100.00),
                Friend(userId = "4", name = "Ana Oliveira", totalBalance = -30.25),
                Friend(userId = "5", name = "Carlos Lima", totalBalance = 0.0)
            )
        }
        
        val filteredFriends = friends.filter { 
            it.name.contains(searchQuery, ignoreCase = true) 
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text(stringResource(Res.string.search_friends_placeholder)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(Res.string.search_content_description)
                        )
                    },
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                FloatingActionButton(
                    onClick = { /* TODO: Add friend action */ },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(Res.string.add_friend_content_description)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Debt information row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Debt text
                Text(
                    text = if (totalDebt < 0) {
                        stringResource(Res.string.you_owe, kotlin.math.abs(totalDebt).toString())
                    } else if (totalDebt > 0) {
                        stringResource(Res.string.they_owe_you, totalDebt.toString())
                    } else {
                        stringResource(Res.string.no_pending_debts)
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (totalDebt < 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
                
                // Filter button
                IconButton(
                    onClick = { /* TODO: Filter action */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = stringResource(Res.string.filter_content_description)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Friends list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredFriends) { friend ->
                    FriendCard(
                        friend = friend,
                        onClick = { /* TODO: Friend click action */ }
                    )
                }
            }
        }
    }
}
