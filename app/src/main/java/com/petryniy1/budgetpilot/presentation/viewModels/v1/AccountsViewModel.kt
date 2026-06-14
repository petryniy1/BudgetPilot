package com.petryniy1.budgetpilot.presentation.viewModels.v1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.results.AccountActionResult
import com.petryniy1.budgetpilot.domain.repository.AccountRepository
import com.petryniy1.budgetpilot.domain.service.AccountManager
import com.petryniy1.budgetpilot.presentation.mapper.toAccountActionUiState
import com.petryniy1.budgetpilot.presentation.uiState.AccountActionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val accountManager: AccountManager
) : ViewModel() {
    private val _accountActionState =
        MutableStateFlow<AccountActionUiState>(AccountActionUiState.Ready)

    val accountActionState: StateFlow<AccountActionUiState> =
        _accountActionState.asStateFlow()

    val accounts: StateFlow<List<Account>> =
        accountRepository.observeAccounts()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    private fun executeAccountAction(
        successMessage: String,
        action: suspend () -> AccountActionResult
    ) {
        if (_accountActionState.value == AccountActionUiState.Loading) return

        viewModelScope.launch {
            _accountActionState.value = AccountActionUiState.Loading

            val result = action()

            _accountActionState.value = result.toAccountActionUiState(successMessage)
        }
    }

    fun createAccount(account: Account) {
        executeAccountAction(
            successMessage = "Account created"
        ) {
            accountManager.createAccount(account)
        }
    }

    fun updateAccount(account: Account) {
        executeAccountAction(
            successMessage = "Account updated"
        ) {
            accountManager.updateAccount(account)
        }
    }

    fun deleteAccount(id: Int) {
        executeAccountAction(
            successMessage = "Account deleted"
        ) {
            accountManager.deleteAccount(id)
        }
    }

    fun clearAccountActionState() {
        _accountActionState.value = AccountActionUiState.Ready
    }
}