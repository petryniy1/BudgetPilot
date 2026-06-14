package com.petryniy1.budgetpilot.presentation.view.v1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.petryniy1.budgetpilot.domain.models.AccountType
import com.petryniy1.budgetpilot.domain.models.CurrencyCode
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
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if ((state.accountId == null)) {
                    "Add account"
                } else {
                    "Edit account"
                }
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = onNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Account name")
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    value = state.balanceInput,
                    onValueChange = onBalanceChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = "Balance")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    )
                )

                Text("Account type")

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

                Text("Currency")

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
        },
        confirmButton = {
            TextButton(onClick = onSave) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun AccountType.toEditorLabel(): String {
    return when (this) {
        AccountType.CASH -> "Cash"
        AccountType.BANK_ACCOUNT -> "Bank account"
    }
}
