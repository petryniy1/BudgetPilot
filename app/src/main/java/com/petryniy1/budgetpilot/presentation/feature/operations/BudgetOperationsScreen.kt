package com.petryniy1.budgetpilot.presentation.feature.operations

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.AccountType
import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.domain.models.OperationType
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotAmountShadow
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotMetaTextStyle
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotPrimaryCardGradient
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotScreenGradient
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextPrimary
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextSecondary
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextShadow
import com.petryniy1.budgetpilot.presentation.design.budgetPilotOutline
import com.petryniy1.budgetpilot.presentation.design.components.BudgetPilotDialog
import com.petryniy1.budgetpilot.presentation.design.components.BudgetPilotDropdownMenu
import com.petryniy1.budgetpilot.presentation.design.components.BudgetPilotDropdownMenuItem
import com.petryniy1.budgetpilot.presentation.design.components.GradientAddButton
import com.petryniy1.budgetpilot.presentation.formatter.formatForDisplay
import com.petryniy1.budgetpilot.presentation.formatter.formatForOperationDisplay
import com.petryniy1.budgetpilot.presentation.mapper.toListItemUiModel
import com.petryniy1.budgetpilot.presentation.mapper.toOperationsSummaryUiModel
import com.petryniy1.budgetpilot.presentation.uiModels.BudgetOperationListItemUiModel
import com.petryniy1.budgetpilot.presentation.uiModels.OperationCurrencyTotalUiModel
import com.petryniy1.budgetpilot.presentation.uiModels.OperationsSummaryUiModel

@Composable
fun BudgetOperationsScreen(
    operations: List<BudgetOperation>,
    accounts: List<Account>,
    onAddOperationClick: () -> Unit,
    onOperationClick: (Int) -> Unit,
    onDeleteOperationClick: (Int) -> Unit
) {
    var operationPendingDelete by remember {
        mutableStateOf<BudgetOperationListItemUiModel?>(null)
    }

    val operationItems = remember(operations, accounts) {
        operations.map { operation ->
            operation.toListItemUiModel(accounts)
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

                BudgetPilotDropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    BudgetPilotDropdownMenuItem(
                        text = "Edit",
                        onClick = {
                            menuExpanded = false
                            onClick(operation)
                        }
                    )

                    BudgetPilotDropdownMenuItem(
                        text = "Delete",
                        isDestructive = true,
                        onClick = {
                            menuExpanded = false
                            onDeleteClick(operation)
                        }
                    )
                }
            }

            Text(
                text = operation.formatAmountForDisplay(),
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
            append(operation.accountCurrencyCode)
            append(" - ")
            append(operation.accountType.toDisplayText())
            append(" - ")
            append(operation.type.formatForDisplay())
        },
        fontSize = 12.sp,
        color = BudgetPilotTextSecondary,
        style = BudgetPilotMetaTextStyle
    )
}

@Composable
private fun DeleteOperationDialog(
    operation: BudgetOperationListItemUiModel,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    BudgetPilotDialog(
        title = "Delete operation",
        confirmText = "Delete",
        onConfirm = {
            onConfirm(operation.id)
        },
        onDismiss = onDismiss,
        isDestructive = true
    ) {
        Text(
            text = "Are you sure you want to delete ${operation.title}?",
            color = BudgetPilotTextSecondary
        )
    }
}

private fun OperationType.amountColor(): Color {
    return when (this) {
        OperationType.EXPENSE -> Color(0xFFFF1435)
        OperationType.INCOME -> Color(0xFF38E07B)
        OperationType.TRANSFER -> Color(0xFFD6DCE8)
    }
}

private fun AccountType.toDisplayText(): String {
    return when (this) {
        AccountType.CASH -> "Cash"
        AccountType.BANK_ACCOUNT -> "Bank account"
    }
}

private fun BudgetOperationListItemUiModel.formatAmountForDisplay(): String {
    val sign = when (type) {
        OperationType.EXPENSE -> "-"
        OperationType.INCOME -> ""
        OperationType.TRANSFER -> ""
    }

    return "$sign${amount.formatForDisplay()}"
}
