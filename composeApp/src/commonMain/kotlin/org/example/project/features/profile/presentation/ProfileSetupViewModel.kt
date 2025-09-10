package org.example.project.features.profile.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.core.firebase.usecase.CreateUserProfileUseCase
import org.example.project.core.firebase.repository.FirebaseUserRepository

/**
 * ViewModel for profile setup and editing
 */
class ProfileSetupViewModel(
    private val createUserProfileUseCase: CreateUserProfileUseCase,
    private val userRepository: FirebaseUserRepository,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {
    private val _uiState = MutableStateFlow(ProfileSetupUiState())
    val uiState: StateFlow<ProfileSetupUiState> = _uiState.asStateFlow()
    
    private var usernameCheckJob: Job? = null
    
    init {
        loadCurrentUserProfile()
    }
    
    private fun loadCurrentUserProfile() {
        coroutineScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val result = userRepository.getCurrentUserProfile()
                result.fold(
                    onSuccess = { user ->
                        if (user != null) {
                            _uiState.value = _uiState.value.copy(
                                isEditMode = true,
                                email = user.email,
                                name = user.name,
                                phoneNumber = user.phoneNumber ?: "",
                                username = user.username ?: "",
                                isSearchableByPhone = user.isSearchableByPhone,
                                isSearchableByEmail = user.isSearchableByEmail,
                                isSearchableByUsername = user.isSearchableByUsername,
                                isLoading = false
                            )
                        } else {
                            _uiState.value = _uiState.value.copy(
                                isEditMode = false,
                                isLoading = false
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }
    
    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(
            name = name,
            nameError = validateName(name)
        )
        updateCanSave()
    }
    
    fun updatePhone(phone: String) {
        _uiState.value = _uiState.value.copy(
            phoneNumber = phone,
            phoneError = validatePhone(phone)
        )
        updateCanSave()
    }
    
    fun updateUsername(username: String) {
        val cleanUsername = username.lowercase().replace(Regex("[^a-z0-9_]"), "")
        _uiState.value = _uiState.value.copy(
            username = cleanUsername,
            usernameError = validateUsername(cleanUsername),
            isUsernameAvailable = null
        )
        updateCanSave()
    }
    
    fun checkUsernameAvailability(username: String) {
        if (username.length < 3 || validateUsername(username) != null) {
            return
        }
        
        usernameCheckJob?.cancel()
        usernameCheckJob = coroutineScope.launch {
            _uiState.value = _uiState.value.copy(isCheckingUsername = true)
            
            try {
                delay(500) // Debounce
                
                val result = createUserProfileUseCase.validateUsername(username)
                result.fold(
                    onSuccess = { isAvailable ->
                        _uiState.value = _uiState.value.copy(
                            isCheckingUsername = false,
                            isUsernameAvailable = isAvailable
                        )
                        updateCanSave()
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isCheckingUsername = false,
                            usernameError = error.message
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isCheckingUsername = false,
                    usernameError = e.message
                )
            }
        }
    }
    
    fun saveProfile() {
        val currentState = _uiState.value
        
        if (!currentState.canSave) return
        
        coroutineScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )
            
            try {
                val result = createUserProfileUseCase.execute(
                    uid = "current_user", // This should come from auth
                    name = currentState.name,
                    email = currentState.email,
                    phoneNumber = currentState.phoneNumber.ifBlank { null },
                    username = currentState.username.ifBlank { null }
                )
                
                result.fold(
                    onSuccess = {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isProfileComplete = true
                        )
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }
    
    private fun validateName(name: String): String? {
        return when {
            name.isBlank() -> "Nome é obrigatório"
            name.length < 2 -> "Nome deve ter pelo menos 2 caracteres"
            name.length > 50 -> "Nome deve ter no máximo 50 caracteres"
            else -> null
        }
    }
    
    private fun validatePhone(phone: String): String? {
        if (phone.isBlank()) return null // Phone is optional
        
        val cleanPhone = phone.replace(Regex("[^0-9]"), "")
        return when {
            cleanPhone.length < 10 -> "Número de telefone inválido"
            cleanPhone.length > 15 -> "Número de telefone muito longo"
            else -> null
        }
    }
    
    private fun validateUsername(username: String): String? {
        if (username.isBlank()) return null // Username is optional
        
        return when {
            username.length < 3 -> "Username deve ter pelo menos 3 caracteres"
            username.length > 20 -> "Username deve ter no máximo 20 caracteres"
            !username.matches(Regex("^[a-z0-9_]+$")) -> "Username pode conter apenas letras, números e _"
            else -> null
        }
    }
    
    private fun updateCanSave() {
        val currentState = _uiState.value
        val canSave = currentState.nameError == null &&
                currentState.phoneError == null &&
                currentState.usernameError == null &&
                currentState.name.isNotBlank() &&
                (currentState.username.isBlank() || currentState.isUsernameAvailable == true) &&
                !currentState.isCheckingUsername
        
        _uiState.value = _uiState.value.copy(canSave = canSave)
    }
}

/**
 * UI state for profile setup screen
 */
data class ProfileSetupUiState(
    val isLoading: Boolean = false,
    val isEditMode: Boolean = false,
    val isProfileComplete: Boolean = false,
    
    // User data
    val email: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val username: String = "",
    
    // Privacy settings
    val isSearchableByPhone: Boolean = true,
    val isSearchableByEmail: Boolean = true,
    val isSearchableByUsername: Boolean = true,
    
    // Validation
    val nameError: String? = null,
    val phoneError: String? = null,
    val usernameError: String? = null,
    val errorMessage: String? = null,
    
    // Username availability
    val isCheckingUsername: Boolean = false,
    val isUsernameAvailable: Boolean? = null,
    
    // UI state
    val canSave: Boolean = false
)
