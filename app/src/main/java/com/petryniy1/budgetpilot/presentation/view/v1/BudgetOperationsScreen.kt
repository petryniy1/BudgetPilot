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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.domain.models.CurrencyCode
import com.petryniy1.budgetpilot.domain.models.Money
import com.petryniy1.budgetpilot.domain.models.OperationType
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotAmountShadow
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotMetaTextStyle
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotPrimaryCardGradient
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotScreenGradient
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextPrimary
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextSecondary
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextShadow
import com.petryniy1.budgetpilot.presentation.design.budgetPilotOutline
import com.petryniy1.budgetpilot.presentation.design.components.GradientAddButton
import com.petryniy1.budgetpilot.presentation.formatter.formatForDisplay
import com.petryniy1.budgetpilot.presentation.formatter.formatForOperationDisplay
import com.petryniy1.budgetpilot.presentation.mapper.toListItemUiModel
import com.petryniy1.budgetpilot.presentation.mapper.toOperationsSummaryUiModel
import com.petryniy1.budgetpilot.presentation.uiModels.BudgetOperationListItemUiModel
import com.petryniy1.budgetpilot.presentation.uiModels.OperationCurrencyTotalUiModel
import com.petryniy1.budgetpilot.presentation.uiModels.OperationsSummaryUiModel
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

    val summary = remember(operations) {
        operations.toOperationsSummaryUiModel()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BudgetPilotScreenGradient)
            .padding(12.dp)
    ) {
        BudgetOperationsHeader(
            operationsCount = operationItems.size,
            onAddOperationClick = onAddOperationClick
        )

        OperationActionMessage(actionState = actionState)

        Spacer(modifier = Modifier.height(2.dp))

        BudgetOperationsContent(
            summary = summary,
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
            .height(80.dp)
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Operations",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = BudgetPilotTextPrimary,
                style = TextStyle(
                    shadow = BudgetPilotTextShadow
                )
            )

            Text(
                text = "$operationsCount operations in",
                modifier = Modifier.weight(1f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = Color(0xFFD6E4FF),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    shadow = BudgetPilotTextShadow
                )
            )

            GradientAddButton(
                onClick = onAddOperationClick
            )
        }
    }
}

@Composable
fun OperationsSummarySection(
    summary: OperationsSummaryUiModel
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OperationsSummaryCard(
            title = "INCOMES: ",
            totals = summary.incomeTotals
        )

        OperationsSummaryCard(
            title = "EXPENSES: ",
            totals = summary.expenseTotals
        )
    }
}

@Composable
private fun OperationsSummaryCard(
    title: String,
    totals: List<OperationCurrencyTotalUiModel>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .budgetPilotOutline(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = BudgetPilotTextPrimary
                )

                totals.forEach { total ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = total.currencyCode,
                            fontStyle = FontStyle.Italic,
                            color = BudgetPilotTextPrimary
                        )

                        Text(
                            text = total.amountText,
                            color = BudgetPilotTextPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BudgetOperationsContent(
    summary: OperationsSummaryUiModel,
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
            item(key = "operations_summary") {
                OperationsSummarySection(
                    summary = summary
                )
            }
            if (operationItems.isEmpty()) {
                item(
                    key = "empty_operations"
                ) {
                    EmptyOperationsState()
                }
            } else {
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
            .budgetPilotOutline()
            .clickable { onClick(operation) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BudgetPilotPrimaryCardGradient)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 96.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            color = Color(0xFF002967),
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = operation.categoryIconRes),
                        contentDescription = operation.categoryName,
                        modifier = Modifier.size(28.dp),
                        colorFilter = ColorFilter.tint(BudgetPilotTextPrimary)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = operation.title,
                        color = BudgetPilotTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(
                            shadow = BudgetPilotTextShadow
                        )
                    )

                    OperationMetaInfo(
                        operation = operation
                    )

                    Text(
                        text = operation.date.formatForOperationDisplay(),
                        style = BudgetPilotMetaTextStyle
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
                        color = BudgetPilotTextPrimary,
                        fontSize = 24.sp,
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
                color = operation.type.amountColor(),
                style = TextStyle(
                    shadow = BudgetPilotAmountShadow
                )
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
        color = BudgetPilotTextSecondary,
        style = BudgetPilotMetaTextStyle
    )
}

private fun OperationType.amountColor(): Color {
    return when (this) {
        OperationType.EXPENSE -> Color(0xFFFF1435)
        OperationType.INCOME -> Color(0xFF38E07B)
        OperationType.TRANSFER -> Color(0xFFD6DCE8)
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
                    amountMinor = 513556000000,
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
