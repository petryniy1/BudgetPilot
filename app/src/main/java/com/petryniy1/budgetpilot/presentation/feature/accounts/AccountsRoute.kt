package com.petryniy1.budgetpilot.presentation.feature.accounts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.CurrencyCode
import com.petryniy1.budgetpilot.domain.models.Money
import com.petryniy1.budgetpilot.presentation.design.components.BudgetPilotSnackbar
import com.petryniy1.budgetpilot.presentation.uiState.AccountActionError
import com.petryniy1.budgetpilot.presentation.uiState.AccountActionUiState
import com.petryniy1.budgetpilot.presentation.uiState.AccountEditorUiState
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

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    fun updateEditorState(
        transform: (AccountEditorUiState) -> AccountEditorUiState
    ) {
        editorState = editorState?.let(transform)
    }

    LaunchedEffect(actionState.value) {
        when (val state = actionState.value) {
            is AccountActionUiState.Success -> {
                editorState = null
                snackbarHostState.showSnackbar(state.message)
                viewModel.clearAccountActionState()
            }

            is AccountActionUiState.Error -> {
                when (state.reason) {
                    AccountActionError.DuplicateAccountName -> {
                        updateEditorState { current ->
                            current.copy(
                                nameError = "Account with this name already exists"
                            )
                        }
                    }

                    AccountActionError.EmptyName -> {
                        updateEditorState { current ->
                            current.copy(
                                nameError = "Account name cannot be empty"
                            )
                        }
                    }

                    AccountActionError.NegativeBalance -> {
                        updateEditorState { current ->
                            current.copy(
                                balanceError = "Balance cannot be negative"
                            )
                        }
                    }

                    AccountActionError.AccountNotFound,
                    AccountActionError.Unexpected -> {
                        snackbarHostState.showSnackbar(state.reason.toMessage())
                    }
                }

                viewModel.clearAccountActionState()
            }

            AccountActionUiState.Ready,
            AccountActionUiState.Loading -> Unit
        }
    }

    editorState?.let { state ->
        AccountEditorDialog(
            state = state,
            onNameChange = { name ->
                updateEditorState { current ->
                    current.copy(
                        name = name,
                        nameError = null
                    )
                }
            },
            onBalanceChange = { balanceInput ->
                updateEditorState { current ->
                    current.copy(
                        balanceInput = balanceInput,
                        balanceError = null
                    )
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

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AccountsScreen(
            accounts = accounts.value,
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

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 120.dp)
                .padding(horizontal = 24.dp),
            snackbar = { snackbarData ->
                BudgetPilotSnackbar(snackbarData = snackbarData)
            }
        )
    }
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

private fun AccountActionError.toMessage(): String {
    return when (this) {
        AccountActionError.AccountNotFound -> "Account not found"
        AccountActionError.DuplicateAccountName -> "Account name already exists"
        AccountActionError.EmptyName -> "Account name cannot be empty"
        AccountActionError.NegativeBalance -> "Balance cannot be negative"
        AccountActionError.Unexpected -> "Unexpected error"
    }
}
