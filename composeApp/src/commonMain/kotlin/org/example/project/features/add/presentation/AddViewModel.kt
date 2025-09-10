package org.example.project.features.add.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.example.project.features.friends.domain.model.Friend
import org.example.project.features.groups.domain.model.Group
import org.example.project.features.add.domain.model.Expense
import org.example.project.features.add.domain.model.ExpenseType
import kotlin.time.Clock

class AddViewModel {
    var uiState by mutableStateOf(AddUiState())
        private set
    
    // Sample data - in a real app, this would come from repositories
    private val sampleFriends = listOf(
        Friend(userId = "1", name = "João Silva", totalBalance = 25.50),
        Friend(userId = "2", name = "Maria Santos", totalBalance = -75.00),
        Friend(userId = "3", name = "Pedro Costa", totalBalance = 100.00),
        Friend(userId = "4", name = "Ana Oliveira", totalBalance = -30.25),
        Friend(userId = "5", name = "Carlos Lima", totalBalance = 0.0)
    )
    
    private val sampleGroups = listOf(
        Group(
            groupId = "1", 
            name = "Viagem Rio", 
            description = "Despesas da viagem ao Rio de Janeiro",
            memberIds = listOf("1", "2", "3"),
            totalBalance = 450.75,
            createdBy = "1",
            createdAtEpochMillis = 1694371200000L // Static timestamp for sample data
        ),
        Group(
            groupId = "2", 
            name = "Casa Compartilhada", 
            description = "Despesas da casa",
            memberIds = listOf("2", "4", "5"),
            totalBalance = -120.30,
            createdBy = "2",
            createdAtEpochMillis = 1694371200000L // Static timestamp for sample data
        ),
        Group(
            groupId = "3", 
            name = "Trabalho", 
            description = "Almoços e cafés do trabalho",
            memberIds = listOf("1", "3", "4"),
            totalBalance = 85.50,
            createdBy = "3",
            createdAtEpochMillis = 1694371200000L // Static timestamp for sample data
        )
    )
    
    init {
        uiState = uiState.copy(
            friends = sampleFriends,
            groups = sampleGroups
        )
    }
    
    fun selectTab(tab: SelectionTab) {
        uiState = uiState.copy(selectedTab = tab)
    }
    
    fun selectFriend(friend: Friend) {
        uiState = uiState.copy(
            selectedFriend = friend,
            selectedGroup = null,
            showExpenseForm = true
        )
    }
    
    fun selectGroup(group: Group) {
        uiState = uiState.copy(
            selectedGroup = group,
            selectedFriend = null,
            showExpenseForm = true
        )
    }
    
    fun updateExpenseName(name: String) {
        uiState = uiState.copy(
            expenseName = name,
            expenseNameError = null
        )
    }
    
    fun updateExpenseDescription(description: String) {
        uiState = uiState.copy(expenseDescription = description)
    }
    
    fun updateExpenseAmount(amount: String) {
        uiState = uiState.copy(
            expenseAmount = amount,
            expenseAmountError = null
        )
    }
    
    fun validateAndSubmitExpense(): Boolean {
        var hasErrors = false
        
        if (uiState.expenseName.isBlank()) {
            uiState = uiState.copy(expenseNameError = "Nome da despesa é obrigatório")
            hasErrors = true
        }
        
        if (uiState.expenseAmount.isBlank()) {
            uiState = uiState.copy(expenseAmountError = "Valor da despesa é obrigatório")
            hasErrors = true
        } else {
            try {
                val amount = uiState.expenseAmount.replace(",", ".").toDouble()
                if (amount <= 0) {
                    uiState = uiState.copy(expenseAmountError = "Valor deve ser maior que zero")
                    hasErrors = true
                }
            } catch (e: NumberFormatException) {
                uiState = uiState.copy(expenseAmountError = "Valor inválido")
                hasErrors = true
            }
        }
        
        if (!hasErrors) {
            // TODO: Submit expense to repository
            resetExpenseForm()
            return true
        }
        
        return false
    }
    
    fun resetExpenseForm() {
        uiState = uiState.copy(
            showExpenseForm = false,
            selectedFriend = null,
            selectedGroup = null,
            expenseName = "",
            expenseDescription = "",
            expenseAmount = "",
            expenseNameError = null,
            expenseAmountError = null
        )
    }
    
    fun goBackToSelection() {
        uiState = uiState.copy(
            showExpenseForm = false,
            selectedFriend = null,
            selectedGroup = null,
            expenseName = "",
            expenseDescription = "",
            expenseAmount = "",
            expenseNameError = null,
            expenseAmountError = null
        )
    }
}

data class AddUiState(
    val selectedTab: SelectionTab = SelectionTab.FRIENDS,
    val friends: List<Friend> = emptyList(),
    val groups: List<Group> = emptyList(),
    val selectedFriend: Friend? = null,
    val selectedGroup: Group? = null,
    val showExpenseForm: Boolean = false,
    val expenseName: String = "",
    val expenseDescription: String = "",
    val expenseAmount: String = "",
    val expenseNameError: String? = null,
    val expenseAmountError: String? = null,
    val isLoading: Boolean = false
)

enum class SelectionTab {
    FRIENDS, GROUPS
}
