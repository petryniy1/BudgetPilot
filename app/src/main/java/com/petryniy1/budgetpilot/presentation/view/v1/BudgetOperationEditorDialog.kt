package com.petryniy1.budgetpilot.presentation.view.v1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.AccountType
import com.petryniy1.budgetpilot.domain.models.OperationType
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotAccentBlue
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotCurrencyFrameOverlay
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotError
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextPrimary
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextSecondary
import com.petryniy1.budgetpilot.presentation.design.components.BudgetPilotDateField
import com.petryniy1.budgetpilot.presentation.design.components.BudgetPilotDialog
import com.petryniy1.budgetpilot.presentation.design.components.BudgetPilotTextField
import com.petryniy1.budgetpilot.presentation.uiState.BudgetOperationEditorUiState
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetOperationEditorDialog(
    state: BudgetOperationEditorUiState,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onAccountChange: (Int) -> Unit,
    onTypeChange: (OperationType) -> Unit,
    onDateChange: (LocalDate) -> Unit,
    onCommentChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    var showDatePicker by remember {
        mutableStateOf(false)
    }

    BudgetPilotDialog(
        title = if (state.operationId == null) {
            "Add operation"
        } else {
            "Edit operation"
        },
        confirmText = "Save",
        onConfirm = onSave,
        onDismiss = onDismiss
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            BudgetPilotTextField(
                value = state.title,
                onValueChange = onTitleChange,
                label = "Title",
                modifier = Modifier.fillMaxWidth(),
                error = state.titleError
            )

            BudgetPilotTextField(
                value = state.amountInput,
                onValueChange = onAmountChange,
                label = "Amount",
                modifier = Modifier.fillMaxWidth(),
                error = state.amountError,
                keyboardType = KeyboardType.Decimal
            )

            DialogSectionTitle(text = "Account")

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.availableAccounts.forEach { account ->
                    AccountSelectorChip(
                        account = account,
                        selected = state.selectedAccountId == account.id,
                        onClick = {
                            onAccountChange(account.id)
                        }
                    )
                }
            }

            state.accountError?.let { error ->
                Text(
                    text = error,
                    color = BudgetPilotError
                )
            }

            DialogSectionTitle(text = "Type")

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OperationType.entries.forEach { type ->
                    OperationTypeSelectorChip(
                        type = type,
                        selected = state.selectedType == type,
                        onClick = {
                            onTypeChange(type)
                        }
                    )
                }
            }

            BudgetPilotDateField(
                value = state.selectedDate.format(OperationDateFormatter),
                label = "Date",
                onClick = {
                    showDatePicker = true
                },
                modifier = Modifier.fillMaxWidth()
            )

            BudgetPilotTextField(
                value = state.comment,
                onValueChange = onCommentChange,
                label = "Comment",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.selectedDate.toUtcMillis()
        )

        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color(0xFF071129)
            ),
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { selectedDateMillis ->
                            onDateChange(selectedDateMillis.toLocalDateFromUtcMillis())
                        }
                        showDatePicker = false
                    }
                ) {
                    Text(
                        text = "Confirm",
                        color = BudgetPilotAccentBlue
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text(
                        text = "Cancel",
                        color = BudgetPilotTextSecondary
                    )
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Color(0xFF071129),
                    titleContentColor = BudgetPilotTextPrimary,
                    headlineContentColor = BudgetPilotTextPrimary,
                    weekdayContentColor = BudgetPilotTextSecondary,
                    subheadContentColor = BudgetPilotTextSecondary,
                    yearContentColor = BudgetPilotTextSecondary,
                    currentYearContentColor = BudgetPilotAccentBlue,
                    selectedYearContainerColor = BudgetPilotAccentBlue,
                    selectedYearContentColor = Color.White,
                    dayContentColor = BudgetPilotTextPrimary,
                    disabledDayContentColor = BudgetPilotTextSecondary.copy(alpha = 0.35f),
                    selectedDayContainerColor = BudgetPilotAccentBlue,
                    selectedDayContentColor = Color.White,
                    todayContentColor = BudgetPilotAccentBlue,
                    todayDateBorderColor = BudgetPilotAccentBlue,
                    navigationContentColor = BudgetPilotTextPrimary
                )
            )
        }
    }
}

@Composable
private fun DialogSectionTitle(
    text: String
) {
    Text(
        text = text,
        color = BudgetPilotTextSecondary
    )
}

@Composable
private fun AccountSelectorChip(
    account: Account,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(text = account.name)
                Text(
                    text = "${account.type.toDisplayLabel()} • ${account.balance.currency.name}",
                    fontSize = 11.sp,
                    color = BudgetPilotTextSecondary
                )
            }
        },
        colors = budgetPilotChipColors()
    )
}

@Composable
private fun OperationTypeSelectorChip(
    type: OperationType,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(text = type.toEditorLabel())
        },
        colors = budgetPilotChipColors()
    )
}

@Composable
private fun budgetPilotChipColors() =
    FilterChipDefaults.filterChipColors(
        selectedContainerColor = BudgetPilotAccentBlue.copy(alpha = 0.22f),
        selectedLabelColor = BudgetPilotTextPrimary,
        containerColor = BudgetPilotCurrencyFrameOverlay,
        labelColor = BudgetPilotTextSecondary
    )

private fun OperationType.toEditorLabel(): String {
    return name.lowercase()
        .replaceFirstChar { firstChar ->
            firstChar.uppercase()
        }
}

private fun AccountType.toDisplayLabel(): String = when (this) {
    AccountType.CASH -> "Cash"
    AccountType.BANK_ACCOUNT -> "Bank account"
}

private val OperationDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd.MM.yyyy")

private fun LocalDate.toUtcMillis(): Long {
    return atStartOfDay(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli()
}

private fun Long.toLocalDateFromUtcMillis(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()
}

