package org.example.project.features.friends.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.core.database.entities.UserEntity
import org.example.project.core.database.entities.ContactEntity
import org.example.project.features.friends.domain.usecase.SearchUsersUseCase
import org.example.project.features.friends.domain.usecase.AddFriendUseCase

class AddFriendViewModel(
    private val searchUsersUseCase: SearchUsersUseCase? = null, // Will be null until we implement repositories
    private val addFriendUseCase: AddFriendUseCase? = null,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {
    private val _uiState = MutableStateFlow(AddFriendUiState())
    val uiState: StateFlow<AddFriendUiState> = _uiState.asStateFlow()
    
    var searchQuery by mutableStateOf("")
        private set
    
    init {
        // Determine if we should show contacts tab (mobile platforms)
        val showContactsTab = isContactsSupported()
        _uiState.value = _uiState.value.copy(
            showContactsTab = showContactsTab,
            selectedTab = if (showContactsTab) AddFriendTab.CONTACTS else AddFriendTab.USERS
        )
        
        if (showContactsTab) {
            checkContactPermission()
        }
    }
    
    fun updateSearchQuery(query: String) {
        searchQuery = query
        _uiState.value = _uiState.value.copy(searchQuery = query)
        
        when (_uiState.value.selectedTab) {
            AddFriendTab.CONTACTS -> searchContacts(query)
            AddFriendTab.USERS -> searchUsers(query)
        }
    }
    
    fun selectTab(tab: AddFriendTab) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
        
        // Trigger search for the new tab
        when (tab) {
            AddFriendTab.CONTACTS -> {
                if (_uiState.value.hasContactPermission) {
                    searchContacts(searchQuery)
                }
            }
            AddFriendTab.USERS -> searchUsers(searchQuery)
        }
    }
    
    fun onContactClick(contact: ContactEntity) {
        if (contact.isRegisteredUser && contact.registeredUserId != null) {
            // Add as friend
            addFriendById(contact.registeredUserId)
        } else {
            // Invite to app
            inviteToApp(contact)
        }
    }
    
    fun onUserClick(user: UserEntity) {
        addFriendById(user.userId)
    }
    
    fun requestContactPermission() {
        // TODO: Implement platform-specific permission request
        // For now, simulate permission granted
        _uiState.value = _uiState.value.copy(hasContactPermission = true)
        loadContacts()
    }
    
    private fun searchContacts(query: String) {
        if (!_uiState.value.hasContactPermission) return
        
        coroutineScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingContacts = true)
            
            // TODO: Implement real contact search
            // For now, use mock data
            val mockContacts = getMockContacts().filter { contact ->
                query.isBlank() || contact.name.contains(query, ignoreCase = true) ||
                contact.phoneNumbers.any { it.contains(query) } ||
                contact.emails.any { it.contains(query, ignoreCase = true) }
            }
            
            _uiState.value = _uiState.value.copy(
                contacts = mockContacts,
                isLoadingContacts = false
            )
        }
    }
    
    private fun searchUsers(query: String) {
        if (query.length < 2) {
            _uiState.value = _uiState.value.copy(users = emptyList())
            return
        }
        
        coroutineScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingUsers = true)
            
            try {
                // TODO: Use real search when repositories are implemented
                // val result = searchUsersUseCase?.invoke(query)
                
                // For now, use mock data
                val mockUsers = getMockUsers().filter { user ->
                    user.name.contains(query, ignoreCase = true) ||
                    user.email?.contains(query, ignoreCase = true) == true ||
                    user.username?.contains(query, ignoreCase = true) == true
                }
                
                _uiState.value = _uiState.value.copy(
                    users = mockUsers,
                    isLoadingUsers = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingUsers = false,
                    error = e.message
                )
            }
        }
    }
    
    private fun addFriendById(userId: String) {
        coroutineScope.launch {
            try {
                // TODO: Use real add friend when repositories are implemented
                // addFriendUseCase?.invoke(userId)
                
                // For now, simulate success
                println("Adding friend with ID: $userId")
                _uiState.value = _uiState.value.copy(
                    message = "Solicitação de amizade enviada!"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Erro ao adicionar amigo"
                )
            }
        }
    }
    
    private fun inviteToApp(contact: ContactEntity) {
        // TODO: Implement app invitation (SMS, email, etc.)
        println("Inviting contact to app: ${contact.name}")
        _uiState.value = _uiState.value.copy(
            message = "Convite enviado para ${contact.name}!"
        )
    }
    
    private fun checkContactPermission() {
        // TODO: Implement platform-specific permission check
        // For now, simulate no permission initially
        _uiState.value = _uiState.value.copy(hasContactPermission = false)
    }
    
    private fun loadContacts() {
        if (!_uiState.value.hasContactPermission) return
        
        coroutineScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingContacts = true)
            
            // TODO: Load real contacts from device
            val contacts = getMockContacts()
            
            _uiState.value = _uiState.value.copy(
                contacts = contacts,
                isLoadingContacts = false
            )
        }
    }
    
    private fun isContactsSupported(): Boolean {
        // TODO: Implement platform detection
        // Return true for mobile platforms (Android/iOS)
        return true // For now, always show contacts tab
    }
    
    // Mock data - remove when real repositories are implemented
    private fun getMockContacts(): List<ContactEntity> {
        return listOf(
            ContactEntity(
                contactId = "1",
                name = "João Silva",
                phoneNumbers = listOf("+55 11 99999-1234"),
                emails = listOf("joao@email.com"),
                isRegisteredUser = true,
                registeredUserId = "user_1",
                lastSyncedEpochMillis = 1694371200000L
            ),
            ContactEntity(
                contactId = "2",
                name = "Maria Santos",
                phoneNumbers = listOf("+55 11 88888-5678"),
                emails = listOf("maria@email.com"),
                isRegisteredUser = false,
                lastSyncedEpochMillis = 1694371200000L
            ),
            ContactEntity(
                contactId = "3",
                name = "Pedro Costa",
                phoneNumbers = listOf("+55 11 77777-9012"),
                emails = listOf("pedro@email.com"),
                isRegisteredUser = true,
                registeredUserId = "user_3",
                lastSyncedEpochMillis = 1694371200000L
            )
        )
    }
    
    private fun getMockUsers(): List<UserEntity> {
        return listOf(
            UserEntity(
                userId = "user_10",
                name = "Ana Oliveira",
                email = "ana@email.com",
                username = "ana_oliveira",
                createdAtEpochMillis = 1694371200000L
            ),
            UserEntity(
                userId = "user_11",
                name = "Carlos Lima",
                email = "carlos@email.com",
                username = "carlos_lima",
                createdAtEpochMillis = 1694371200000L
            ),
            UserEntity(
                userId = "user_12",
                name = "Beatriz Ferreira",
                email = "beatriz@email.com",
                username = "bia_ferreira",
                createdAtEpochMillis = 1694371200000L
            )
        )
    }
    
    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class AddFriendUiState(
    val selectedTab: AddFriendTab = AddFriendTab.USERS,
    val showContactsTab: Boolean = false,
    val searchQuery: String = "",
    val contacts: List<ContactEntity> = emptyList(),
    val users: List<UserEntity> = emptyList(),
    val hasContactPermission: Boolean = false,
    val isLoadingContacts: Boolean = false,
    val isLoadingUsers: Boolean = false,
    val error: String? = null,
    val message: String? = null
)

enum class AddFriendTab {
    CONTACTS, USERS
}
