package org.example.project.features.friends.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.example.project.features.friends.domain.model.Friend
import org.example.project.features.friends.domain.usecase.GetFriendsUseCase
import org.example.project.features.friends.domain.usecase.SearchFriendsUseCase
import org.example.project.features.friends.domain.usecase.GetTotalDebtSummaryUseCase

class FriendsViewModel(
    private val getFriendsUseCase: GetFriendsUseCase,
    private val searchFriendsUseCase: SearchFriendsUseCase,
    private val getTotalDebtSummaryUseCase: GetTotalDebtSummaryUseCase,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {
    private val _uiState = MutableStateFlow(FriendsUiState())
    val uiState: StateFlow<FriendsUiState> = _uiState.asStateFlow()
    
    var searchQuery by mutableStateOf("")
        private set
    
    init {
        loadFriends()
        loadTotalDebtSummary()
    }
    
    fun updateSearchQuery(query: String) {
        searchQuery = query
        searchFriends(query)
    }
    
    fun onAddFriendClick() {
        _uiState.value = _uiState.value.copy(showAddFriendScreen = true)
    }
    
    fun onAddFriendScreenDismiss() {
        _uiState.value = _uiState.value.copy(showAddFriendScreen = false)
    }
    
    fun onFriendClick(friend: Friend) {
        // TODO: Navigate to friend detail screen or expense history
        println("Friend clicked: ${friend.name}")
    }
    
    fun onFilterClick() {
        // TODO: Implement filter functionality
        println("Filter clicked")
    }
    
    private fun loadFriends() {
        coroutineScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                getFriendsUseCase().collect { friends ->
                    _uiState.value = _uiState.value.copy(
                        friends = friends,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }
    
    private fun searchFriends(query: String) {
        coroutineScope.launch {
            try {
                searchFriendsUseCase(query).collect { friends ->
                    _uiState.value = _uiState.value.copy(
                        friends = friends,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Search failed"
                )
            }
        }
    }
    
    private fun loadTotalDebtSummary() {
        coroutineScope.launch {
            try {
                getTotalDebtSummaryUseCase().collect { totalDebt ->
                    _uiState.value = _uiState.value.copy(totalDebt = totalDebt)
                }
            } catch (e: Exception) {
                // Don't show error for debt summary, just keep default value
                println("Failed to load debt summary: ${e.message}")
            }
        }
    }
    
    fun refresh() {
        loadFriends()
        loadTotalDebtSummary()
    }
}

data class FriendsUiState(
    val friends: List<Friend> = emptyList(),
    val totalDebt: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showAddFriendScreen: Boolean = false
)
