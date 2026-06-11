package com.petryniy1.budgetpilot.presentation.view.v1

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.petryniy1.budgetpilot.presentation.viewModels.v1.AccountsViewModel

@Composable
fun AccountsRoute(
    viewModel: AccountsViewModel = hiltViewModel()
) {
    val accounts = viewModel.accounts.collectAsStateWithLifecycle()
    val actionState = viewModel.accountActionState.collectAsStateWithLifecycle()

    AccountsScreen(
        accounts = accounts.value,
        actionState = actionState.value,
        onAddAccountClick = {
            // TODO Open add account screen.
        },
        onAccountClick = { _ ->
            // TODO Open account details or edit screen.
        },
        onDeleteAccountClick = { accountId ->
            viewModel.deleteAccount(accountId)
        }
    )
}