package com.petryniy1.budgetpilot.presentation.view.v1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.OperationType
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotAccentBlue
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotCurrencyFrameOverlay
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotError
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextPrimary
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextSecondary
import com.petryniy1.budgetpilot.presentation.design.components.BudgetPilotDialog
import com.petryniy1.budgetpilot.presentation.design.components.BudgetPilotTextField
import com.petryniy1.budgetpilot.presentation.uiState.BudgetOperationEditorUiState
import java.time.format.DateTimeFormatter

@Composable
fun BudgetOperationEditorDialog(
    state: BudgetOperationEditorUiState,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onAccountChange: (Int) -> Unit,
    onTypeChange: (OperationType) -> Unit,
    onCommentChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
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

            BudgetPilotTextField(
                value = state.selectedDate.format(OperationDateFormatter),
                onValueChange = {},
                label = "Date",
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )

            BudgetPilotTextField(
                value = state.comment,
                onValueChange = onCommentChange,
                label = "Comment",
                modifier = Modifier.fillMaxWidth()
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
            Text(text = account.name)
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

private val OperationDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd.MM.yyyy")

