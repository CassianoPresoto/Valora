package org.example.project.features.activities.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import org.example.project.core.firebase.usecase.expense.ObserveAllExpensesUseCase
import org.example.project.core.firebase.model.FirebaseExpense
import org.koin.compose.koinInject
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp

object ActivitiesScreen {
    @Composable
    fun Content(innerPadding: PaddingValues) {
        val observeAll: ObserveAllExpensesUseCase = koinInject()
        val expenses by observeAll().collectAsState(initial = emptyList())

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Atividades",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
            if (expenses.isEmpty()) {
                item {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Sem atividades recentes", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                items(expenses) { exp ->
                    ExpenseItem(exp)
                }
            }
        }
    }

    @Composable
    private fun ExpenseItem(expense: FirebaseExpense) {
        Card(
            modifier = Modifier.padding(vertical = 6.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            androidx.compose.foundation.layout.Column(Modifier.padding(12.dp)) {
                Text(text = expense.name, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text(
                    text = "Total: R$ ${expense.totalAmount}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Pagou: ${expense.payerId}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
