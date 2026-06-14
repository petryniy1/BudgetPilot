package com.petryniy1.budgetpilot.presentation.view.v1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.petryniy1.budgetpilot.domain.models.AccountType
import com.petryniy1.budgetpilot.domain.models.CurrencyCode
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextPrimary
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextSecondary
import com.petryniy1.budgetpilot.presentation.design.components.BudgetPilotDialog
import com.petryniy1.budgetpilot.presentation.uiState.AccountEditorUiState

@Composable
fun AccountEditorDialog(
    state: AccountEditorUiState,
    onNameChange: (String) -> Unit,
    onBalanceChange: (String) -> Unit,
    onAccountTypeChange: (AccountType) -> Unit,
    onCurrencyChange: (CurrencyCode) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    BudgetPilotDialog(
        title = if (state.accountId == null) {
            "Add account"
        } else {
            "Edit account"
        },
        confirmText = "Save",
        onConfirm = onSave,
        onDismiss = onDismiss
    ) {
        OutlinedTextField(
            value = state.name,
            onValueChange = onNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Account name")
            },
            singleLine = true,
            isError = state.nameError != null,
            supportingText = state.nameError?.let { error ->
                {
                    Text(text = error)
                }
            },
            colors = budgetPilotTextFieldColors()
        )

        OutlinedTextField(
            value = state.balanceInput,
            onValueChange = onBalanceChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Balance")
            },
            singleLine = true,
            isError = state.balanceError != null,
            supportingText = state.balanceError?.let { error ->
                {
                    Text(text = error)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            colors = budgetPilotTextFieldColors()
        )

        Text(
            text = "Account type",
            color = BudgetPilotTextSecondary,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AccountType.entries.forEach { accountType ->
                FilterChip(
                    selected = state.accountType == accountType,
                    onClick = {
                        onAccountTypeChange(accountType)
                    },
                    label = {
                        Text(accountType.toEditorLabel())
                    }
                )
            }
        }

        Text(
            text = "Currency",
            color = BudgetPilotTextSecondary,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CurrencyCode.entries.forEach { currency ->
                FilterChip(
                    selected = state.currency == currency,
                    onClick = {
                        onCurrencyChange(currency)
                    },
                    label = {
                        Text(currency.name)
                    }
                )
            }
        }
    }
}

@Composable
private fun budgetPilotTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = BudgetPilotTextPrimary,
    unfocusedTextColor = BudgetPilotTextPrimary,
    focusedLabelColor = Color(0xFF3DBDFF),
    unfocusedLabelColor = BudgetPilotTextSecondary,
    focusedBorderColor = Color(0xFF3DBDFF),
    unfocusedBorderColor = Color(0x66D6E4FF),
    cursorColor = Color(0xFF3DBDFF),
    errorTextColor = Color(0xFFFF6B6B),
    errorLabelColor = Color(0xFFFF6B6B),
    errorBorderColor = Color(0xFFFF6B6B)
)

private fun AccountType.toEditorLabel(): String {
    return when (this) {
        AccountType.CASH -> "Cash"
        AccountType.BANK_ACCOUNT -> "Bank account"
    }
}