package com.petryniy1.budgetpilot.presentation.view.v1

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.domain.models.CurrencyCode
import com.petryniy1.budgetpilot.domain.models.Money
import com.petryniy1.budgetpilot.domain.models.OperationType
import com.petryniy1.budgetpilot.presentation.formatter.formatForDisplay
import com.petryniy1.budgetpilot.presentation.uiState.OperationActionError
import com.petryniy1.budgetpilot.presentation.uiState.OperationActionUiState
import java.time.LocalDate

@Composable
private fun BudgetOperationItem(
    operation: BudgetOperation,
    onClick: (BudgetOperation) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick(operation) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "□",
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = operation.title)

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Category · Account · ${operation.type.name} · ${operation.date}"
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(text = operation.amount.formatForDisplay())

                IconButton(onClick = { menuExpanded = true }) {
                    Text(text = "⋮")
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Edit") },
                        onClick = {
                            menuExpanded = false
                            onClick(operation)
                        }
                    )

                    DropdownMenuItem(
                        text = { Text(text = "Delete") },
                        onClick = {
                            menuExpanded = false
                            onDeleteClick(operation.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun OperationActionMessage(
    actionState: OperationActionUiState
) {
    when (actionState) {
        OperationActionUiState.Ready -> Unit
        OperationActionUiState.Loading -> Text("Loading...")
        OperationActionUiState.Success -> Text("Operation saved")
        is OperationActionUiState.Error -> Text(actionState.reason.toMessage())
    }
}

private fun OperationActionError.toMessage(): String {
    return when (this) {
        OperationActionError.AccountNotFound -> "Account not found"
        OperationActionError.InsufficientFunds -> "Insufficient funds"
        OperationActionError.CurrencyMismatch -> "Currency mismatch"
        OperationActionError.DuplicateOperation -> "Duplicate operation"
        OperationActionError.OperationNotFound -> "Operation not found"
        OperationActionError.InvalidData -> "Invalid operation data"
        OperationActionError.Unexpected -> "Unexpected error"
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

        OperationActionMessage(actionState = actionState)

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


@Preview(showBackground = true)
@Composable
private fun BudgetOperationsScreenPreview() {
    BudgetOperationsScreen(
        operations = listOf(
            BudgetOperation(
                id = 1,
                accountId = 1,
                title = "Biedronka",
                amount = Money(
                    amountMinor = -1200,
                    currency = CurrencyCode.PLN
                ),
                type = OperationType.EXPENSE,
                date = LocalDate.of(2026, 6, 9),
                comment = "Groceries"
            ),
            BudgetOperation(
                id = 2,
                accountId = 1,
                title = "Salary",
                amount = Money(
                    amountMinor = 500000,
                    currency = CurrencyCode.PLN
                ),
                type = OperationType.INCOME,
                date = LocalDate.of(2026, 6, 8),
                comment = "Monthly salary"
            )
        ),
        actionState = OperationActionUiState.Ready,
        onAddOperationClick = {},
        onOperationClick = {},
        onDeleteOperationClick = {}
    )
}
