package com.petryniy1.budgetpilot.presentation.view.v1

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.domain.models.CurrencyCode
import com.petryniy1.budgetpilot.domain.models.Money
import com.petryniy1.budgetpilot.domain.models.OperationType
import com.petryniy1.budgetpilot.presentation.formatter.formatForDisplay
import com.petryniy1.budgetpilot.presentation.formatter.formatForOperationDisplay
import com.petryniy1.budgetpilot.presentation.mapper.toListItemUiModel
import com.petryniy1.budgetpilot.presentation.uiModels.BudgetOperationListItemUiModel
import com.petryniy1.budgetpilot.presentation.uiState.OperationActionError
import com.petryniy1.budgetpilot.presentation.uiState.OperationActionUiState
import java.time.LocalDate

@Composable
fun BudgetOperationsScreen(
    operations: List<BudgetOperation>,
    actionState: OperationActionUiState,
    onAddOperationClick: () -> Unit,
    onOperationClick: (Int) -> Unit,
    onDeleteOperationClick: (Int) -> Unit
) {
    var operationPendingDelete by remember {
        mutableStateOf<BudgetOperationListItemUiModel?>(null)
    }

    val operationItems = remember(operations) {
        operations.map { operation ->
            operation.toListItemUiModel()
        }
    }

    operationPendingDelete?.let { operation ->
        DeleteOperationDialog(
            operation = operation,
            onConfirm = { operationId ->
                operationPendingDelete = null
                onDeleteOperationClick(operationId)
            },
            onDismiss = { operationPendingDelete = null }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F1FA))
            .padding(16.dp)
    ) {
        BudgetOperationsHeader(
            operationsCount = operationItems.size,
            onAddOperationClick = onAddOperationClick
        )

        OperationActionMessage(actionState = actionState)

        BudgetOperationsContent(
            operationItems = operationItems,
            onOperationClick = onOperationClick,
            onDeleteOperationClick = { operation ->
                operationPendingDelete = operation
            }
        )
    }
}

@Composable
private fun BudgetOperationsHeader(
    operationsCount: Int,
    onAddOperationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Operations",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF24162F)
            )

            Text(
                text = "$operationsCount items",
                fontSize = 12.sp,
                color = Color(0xFF6D5A78)
            )

            Button(onClick = onAddOperationClick) {
                Text("Add")
            }

        }
    }
}

@Composable
private fun BudgetOperationsContent(
    operationItems: List<BudgetOperationListItemUiModel>,
    onOperationClick: (Int) -> Unit,
    onDeleteOperationClick: (BudgetOperationListItemUiModel) -> Unit
) {
    val listState = rememberLazyListState()

    if (operationItems.isEmpty()) {
        EmptyOperationsState()
    } else {
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                items = operationItems,
                key = { operation -> operation.id }
            ) { operation ->
                BudgetOperationItem(
                    operation = operation,
                    onClick = { operationItem -> onOperationClick(operationItem.id) },
                    onDeleteClick = onDeleteOperationClick
                )
            }
        }
    }
}

@Composable
private fun EmptyOperationsState() {
   Box(
       modifier = Modifier.fillMaxSize(),
       contentAlignment = Alignment.Center
   ) {
       Text(
           text = "No operations yet",
           fontSize = 24.sp,
           fontWeight = FontWeight.Bold,
           fontStyle = FontStyle.Italic,
           color = Color(0xFF6D5A78)
           )
   }
}

@Composable
private fun BudgetOperationItem(
    operation: BudgetOperationListItemUiModel,
    onClick: (BudgetOperationListItemUiModel) -> Unit,
    onDeleteClick: (BudgetOperationListItemUiModel) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(operation) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE7E0EA)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 88.dp),
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = painterResource(id = operation.categoryIconRes),
                    contentDescription = operation.categoryName,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = operation.title,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    OperationMetaInfo(
                        operation = operation
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .wrapContentSize(Alignment.TopEnd)
            ) {
                IconButton(
                    modifier = Modifier
                        .size(24.dp)
                        .offset(x = 8.dp, y = (-8).dp),
                    onClick = { menuExpanded = true }
                ) {
                    Text(
                        text = "⋮",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
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
                            onDeleteClick(operation)
                        }
                    )
                }
            }

            Text(
                text = operation.amount.formatForDisplay(),
                modifier = Modifier.align(Alignment.BottomEnd),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = operation.type.amountColor()
            )
        }
    }
}

@Composable
private fun OperationMetaInfo(
    operation: BudgetOperationListItemUiModel
) {
    Text(
        text = buildString {
            append(operation.categoryName)
            append(" - ")
            append(operation.accountName)
            append(" - ")
            append(operation.type.formatForDisplay())
            append(" - ")
            append(operation.date.formatForOperationDisplay())
        },
        fontSize = 12.sp,
        color = Color.DarkGray
    )
}

private fun OperationType.amountColor(): Color {
    return when (this) {
        OperationType.EXPENSE -> Color(0xFFB00020)
        OperationType.INCOME -> Color(0xFF0B7A3B)
        OperationType.TRANSFER -> Color(0xFF4B5563)
    }
}

@Composable
private fun DeleteOperationDialog(
    operation: BudgetOperationListItemUiModel,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Delete operation") },
        text = { Text(text = "Are you sure you want to delete ${operation.title}?") },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(operation.id) }
            ) {
                Text(text = "Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
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
                    amountMinor = -1256,
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
                    amountMinor = 513556,
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
