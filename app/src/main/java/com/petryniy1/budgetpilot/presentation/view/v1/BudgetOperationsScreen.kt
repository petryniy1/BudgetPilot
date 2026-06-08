package com.petryniy1.budgetpilot.presentation.view.v1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.presentation.uiState.OperationActionUiState

@Composable
private fun BudgetOperationItem(
    operation: BudgetOperation,
    onClick: (BudgetOperation) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(operation.title)
            Text(
                "${operation.amount.amountMinor} " +
                        operation.amount.currency.name
            )
        }

        Button(onClick = { onDeleteClick(operation.id) }) {
            Text("Delete")
        }
    }
}

@Composable
fun BudgetOperationsScreen(
    operations: List<BudgetOperation>,
    actionState: OperationActionUiState,
    onAddOperationClick: () -> Unit,
    onOperationClick: (BudgetOperation) -> Unit,
    onDeleteOperationClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Operations")

            Button(onAddOperationClick) {
                Text("Add")
            }
        }

        if (operations.isEmpty()) {
            Text("No operations yet")
        } else {
            LazyColumn {
                items(
                    items = operations,
                    key = { operation -> operation.id }
                ) { operation ->
                    BudgetOperationItem(
                        operation = operation,
                        onClick = onOperationClick,
                        onDeleteClick = onDeleteOperationClick
                    )
                }
            }
        }
    }
}
