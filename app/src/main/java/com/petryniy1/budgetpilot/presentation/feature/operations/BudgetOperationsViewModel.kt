package com.petryniy1.budgetpilot.presentation.feature.operations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.domain.repository.AccountRepository
import com.petryniy1.budgetpilot.domain.repository.BudgetOperationRepository
import com.petryniy1.budgetpilot.domain.results.OperationActionResult
import com.petryniy1.budgetpilot.domain.service.BudgetOperationManager
import com.petryniy1.budgetpilot.presentation.mapper.toOperationActionUiState
import com.petryniy1.budgetpilot.presentation.uiState.OperationActionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetOperationsViewModel @Inject constructor(
    private val budgetOperationManager: BudgetOperationManager,
    private val budgetOperationRepository: BudgetOperationRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {
    private val _operationActionState =
        MutableStateFlow<OperationActionUiState>(OperationActionUiState.Ready)
    val operationActionState: StateFlow<OperationActionUiState> =
        _operationActionState.asStateFlow()

    val operations: StateFlow<List<BudgetOperation>> =
        budgetOperationRepository.observeOperations()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val accounts: StateFlow<List<Account>> =
        accountRepository.observeAccounts()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    private fun executeOperationAction(
        action: suspend () -> OperationActionResult
    ) {
        if (_operationActionState.value == OperationActionUiState.Loading) return

        viewModelScope.launch {
            _operationActionState.value = OperationActionUiState.Loading

            val result = action()

            _operationActionState.value = result.toOperationActionUiState()
        }
    }

    fun addOperation(operation: BudgetOperation) {
        executeOperationAction { budgetOperationManager.addOperation(operation) }
    }

    fun updateOperation(operation: BudgetOperation) {
        executeOperationAction { budgetOperationManager.updateOperation(operation) }
    }

    fun deleteOperation(id: Int) {
        executeOperationAction { budgetOperationManager.deleteOperation(id) }
    }

    fun clearOperationActionState() {
        _operationActionState.value = OperationActionUiState.Ready
    }
}

