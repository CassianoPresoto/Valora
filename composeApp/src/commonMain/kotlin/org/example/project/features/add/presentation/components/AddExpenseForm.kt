package org.example.project.features.add.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.features.friends.domain.model.Friend
import org.example.project.features.groups.domain.model.Group
import org.jetbrains.compose.resources.stringResource
import valora.composeapp.generated.resources.Res
import valora.composeapp.generated.resources.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseForm(
    selectedFriend: Friend? = null,
    selectedGroup: Group? = null,
    expenseName: String,
    expenseDescription: String,
    expenseAmount: String,
    payerIsCurrentUser: Boolean,
    splitMode: String,
    isLoading: Boolean,
    submitMessage: String?,
    expenseNameError: String? = null,
    expenseAmountError: String? = null,
    onExpenseNameChange: (String) -> Unit,
    onExpenseDescriptionChange: (String) -> Unit,
    onExpenseAmountChange: (String) -> Unit,
    onPayerChange: (Boolean) -> Unit,
    onSplitModeChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top bar with back button and title
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                text = stringResource(Res.string.add_expense_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Selected friend/group info
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Despesa com: ",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = selectedFriend?.name ?: selectedGroup?.name ?: "",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Expense name field
        OutlinedTextField(
            value = expenseName,
            onValueChange = onExpenseNameChange,
            label = { Text(stringResource(Res.string.expense_name_label)) },
            placeholder = { Text(stringResource(Res.string.expense_name_placeholder)) },
            modifier = Modifier.fillMaxWidth(),
            isError = expenseNameError != null,
            supportingText = expenseNameError?.let { { Text(it) } },
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Expense description field
        OutlinedTextField(
            value = expenseDescription,
            onValueChange = onExpenseDescriptionChange,
            label = { Text(stringResource(Res.string.expense_description_label)) },
            placeholder = { Text(stringResource(Res.string.expense_description_placeholder)) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            minLines = 1
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Expense amount field
        OutlinedTextField(
            value = expenseAmount,
            onValueChange = onExpenseAmountChange,
            label = { Text(stringResource(Res.string.expense_amount_label)) },
            placeholder = { Text(stringResource(Res.string.expense_amount_placeholder)) },
            leadingIcon = { Text(stringResource(Res.string.currency_symbol)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = expenseAmountError != null,
            supportingText = expenseAmountError?.let { { Text(it) } },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Who paid selector
        Text(
            text = "Quem pagou?",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = payerIsCurrentUser,
                onClick = { onPayerChange(true) },
                label = { Text("Você") }
            )
            FilterChip(
                selected = !payerIsCurrentUser,
                onClick = { onPayerChange(false) },
                label = { Text(selectedFriend?.name ?: "Amigo") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Split mode selector
        Text(
            text = "Como dividir?",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = splitMode == "EQUAL",
                onClick = { onSplitModeChange("EQUAL") },
                label = { Text("Igual") }
            )
            FilterChip(
                selected = splitMode == "PAYER_ALL",
                onClick = { onSplitModeChange("PAYER_ALL") },
                label = { Text("Quem pagou tudo") }
            )
            FilterChip(
                selected = splitMode == "OTHER_ALL",
                onClick = { onSplitModeChange("OTHER_ALL") },
                label = { Text("Outro tudo") }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Submit button
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !isLoading && expenseName.isNotBlank() && expenseAmount.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = stringResource(Res.string.add_expense_button),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        submitMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
        }
    }
}
