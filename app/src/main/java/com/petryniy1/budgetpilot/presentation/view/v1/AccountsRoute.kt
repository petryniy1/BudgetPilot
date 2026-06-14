package com.petryniy1.budgetpilot.presentation.view.v1

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.CurrencyCode
import com.petryniy1.budgetpilot.domain.models.Money
import com.petryniy1.budgetpilot.presentation.uiState.AccountActionUiState
import com.petryniy1.budgetpilot.presentation.uiState.AccountEditorUiState
import com.petryniy1.budgetpilot.presentation.viewModels.v1.AccountsViewModel
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun AccountsRoute(
    viewModel: AccountsViewModel = hiltViewModel()
) {
    val accounts = viewModel.accounts.collectAsStateWithLifecycle()
    val actionState = viewModel.accountActionState.collectAsStateWithLifecycle()

    var editorState by remember {
        mutableStateOf<AccountEditorUiState?>(null)
    }

    LaunchedEffect(actionState.value) {
        if (actionState.value == AccountActionUiState.Success) {
            editorState = null
        }
    }

    fun updateEditorState(
        transform: (AccountEditorUiState) -> AccountEditorUiState
    ) {
        editorState = editorState?.let(transform)
    }

    editorState?.let { state ->
        AccountEditorDialog(
            state = state,
            onNameChange = { name ->
                updateEditorState { current ->
                    current.copy(name = name)
                }
            },
            onBalanceChange = { balanceInput ->
                updateEditorState { current ->
                    current.copy(balanceInput = balanceInput)
                }
            },
            onAccountTypeChange = { accountType ->
                updateEditorState { current ->
                    current.copy(accountType = accountType)
                }
            },
            onCurrencyChange = { currency ->
                updateEditorState { current ->
                    current.copy(currency = currency)
                }
            },
            onSave = {
                val currentState = editorState ?: return@AccountEditorDialog
                val account = currentState.toDomainAccountOrNull() ?: return@AccountEditorDialog

                if (currentState.accountId == null) {
                    viewModel.createAccount(account)
                } else {
                    viewModel.updateAccount(account)
                }
            },
            onDismiss = {
                editorState = null
            }
        )
    }

    AccountsScreen(
        accounts = accounts.value,
        actionState = actionState.value,
        onAddAccountClick = {
            editorState = AccountEditorUiState()
        },
        onAccountClick = { accountId ->
            val account = accounts.value.firstOrNull { account ->
                account.id == accountId
            } ?: return@AccountsScreen

            editorState = account.toEditorUiState()
        },
        onDeleteAccountClick = { accountId ->
            viewModel.deleteAccount(accountId)
        }
    )
}

private fun AccountEditorUiState.toDomainAccountOrNull(): Account? {
    val balanceMinor = balanceInput.toMinorAmountOrNull(currency)
        ?: return null

    return Account(
        id = accountId ?: 0,
        name = name.trim(),
        type = accountType,
        balance = Money(
            amountMinor = balanceMinor,
            currency = currency
        )
    )
}

private fun String.toMinorAmountOrNull(
    currency: CurrencyCode
): Long? {
    val normalized = trim()
        .replace(" ", "")
        .replace(",", ".")

    if (normalized.isBlank()) {
        return 0L
    }

    return runCatching {
        BigDecimal(normalized)
            .movePointRight(currency.fractionDigits)
            .setScale(0, RoundingMode.HALF_UP)
            .longValueExact()
    }.getOrNull()
}

private fun Account.toEditorUiState(): AccountEditorUiState {
    return AccountEditorUiState(
        accountId = id,
        name = name,
        accountType = type,
        currency = balance.currency,
        balanceInput = balance.toInputString()
    )
}

private fun Money.toInputString(): String {
    return BigDecimal(amountMinor)
        .movePointLeft(currency.fractionDigits)
        .setScale(currency.fractionDigits, RoundingMode.UNNECESSARY)
        .toPlainString()
}
