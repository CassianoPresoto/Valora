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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.example.project.features.friends.domain.usecase.GetFriendsUseCase
import org.example.project.features.friends.domain.usecase.SearchFriendsUseCase
import org.example.project.features.friends.domain.usecase.GetTotalDebtSummaryUseCase
import org.example.project.features.friends.domain.repository.FriendsRepository

object FriendsScreen {
    @Composable
    @Preview
    fun Content(innerPadding: PaddingValues) {
        // TODO: Inject ViewModel through dependency injection when implemented
        val viewModel = remember { 
            // For now, create with mock repository - will be replaced with real implementations
            val mockRepository = MockFriendsRepository()
            FriendsViewModel(
                getFriendsUseCase = GetFriendsUseCase(mockRepository),
                searchFriendsUseCase = SearchFriendsUseCase(mockRepository),
                getTotalDebtSummaryUseCase = GetTotalDebtSummaryUseCase(mockRepository)
            )
        }
        
        val uiState by viewModel.uiState.collectAsState()
        
        // Show AddFriendScreen if requested
        if (uiState.showAddFriendScreen) {
            AddFriendScreen(
                onBack = viewModel::onAddFriendScreenDismiss
            )
            return
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
                    value = viewModel.searchQuery,
                    onValueChange = viewModel::updateSearchQuery,
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
                    onClick = viewModel::onAddFriendClick,
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
                    text = if (uiState.totalDebt < 0) {
                        stringResource(Res.string.you_owe, kotlin.math.abs(uiState.totalDebt).toString())
                    } else if (uiState.totalDebt > 0) {
                        stringResource(Res.string.they_owe_you, uiState.totalDebt.toString())
                    } else {
                        stringResource(Res.string.no_pending_debts)
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (uiState.totalDebt < 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
                
                // Filter button
                IconButton(
                    onClick = viewModel::onFilterClick
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = stringResource(Res.string.filter_content_description)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Friends list
            Box(modifier = Modifier.weight(1f)) {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    
                    uiState.error != null -> {
                        val errorMessage = uiState.error
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = errorMessage ?: "Erro desconhecido",
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(onClick = viewModel::refresh) {
                                Text("Tentar novamente")
                            }
                        }
                    }
                    
                    uiState.friends.isEmpty() -> {
                        Text(
                            text = "Nenhum amigo encontrado",
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    else -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.friends) { friend ->
                                FriendCard(
                                    friend = friend,
                                    onClick = { viewModel.onFriendClick(friend) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Mock repository implementation for testing - remove when real repositories are implemented
private class MockFriendsRepository : FriendsRepository {
    private val mockFriends = listOf(
        Friend(userId = "1", name = "João Silva", totalBalance = 25.50),
        Friend(userId = "2", name = "Maria Santos", totalBalance = -75.00),
        Friend(userId = "3", name = "Pedro Costa", totalBalance = 100.00),
        Friend(userId = "4", name = "Ana Oliveira", totalBalance = -30.25),
        Friend(userId = "5", name = "Carlos Lima", totalBalance = 0.0)
    )
    
    override fun getFriends(): Flow<List<Friend>> {
        return flowOf(mockFriends)
    }
    
    override fun searchFriends(query: String): Flow<List<Friend>> {
        return if (query.isBlank()) {
            flowOf(mockFriends)
        } else {
            flowOf(mockFriends.filter { it.name.contains(query, ignoreCase = true) })
        }
    }
    
    override suspend fun addFriend(userId: String): Result<Unit> {
        return Result.success(Unit)
    }
    
    override suspend fun removeFriend(friendUserId: String): Result<Unit> {
        return Result.success(Unit)
    }
    
    override suspend fun acceptFriendRequest(friendUserId: String): Result<Unit> {
        return Result.success(Unit)
    }
    
    override suspend fun declineFriendRequest(friendUserId: String): Result<Unit> {
        return Result.success(Unit)
    }
    
    override fun getPendingFriendRequests(): Flow<List<Friend>> {
        return flowOf(emptyList())
    }
    
    override fun getSentFriendRequests(): Flow<List<Friend>> {
        return flowOf(emptyList())
    }
    
    override suspend fun updateFriendBalance(friendUserId: String, totalBalance: Double, personalBalance: Double): Result<Unit> {
        return Result.success(Unit)
    }
    
    override fun getTotalDebtSummary(): Flow<Double> {
        return flowOf(-150.50) // Example: you owe R$ 150.50
    }
}
