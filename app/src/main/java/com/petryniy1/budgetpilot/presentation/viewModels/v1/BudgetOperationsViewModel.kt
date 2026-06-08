package com.petryniy1.budgetpilot.presentation.viewModels.v1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.domain.models.results.OperationActionResult
import com.petryniy1.budgetpilot.domain.service.BudgetOperationManager
import com.petryniy1.budgetpilot.presentation.uiState.OperationActionError
import com.petryniy1.budgetpilot.presentation.uiState.OperationActionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetOperationsViewModel @Inject constructor(
    private val budgetOperationManager: BudgetOperationManager
) : ViewModel() {
    private val _operationActionState =
        MutableStateFlow<OperationActionUiState>(OperationActionUiState.Ready)

    val operationActionState: StateFlow<OperationActionUiState> =
        _operationActionState.asStateFlow()

    fun addOperation(operation: BudgetOperation) {
        viewModelScope.launch {
            _operationActionState.value = OperationActionUiState.Loading

            val result = budgetOperationManager.addOperation(operation)

            _operationActionState.value = when (result) {
                OperationActionResult.Success ->
                    OperationActionUiState.Success

                OperationActionResult.AccountNotFound ->
                    OperationActionUiState.Error(OperationActionError.AccountNotFound)

                OperationActionResult.InsufficientFunds ->
                    OperationActionUiState.Error(OperationActionError.InsufficientFunds)

                OperationActionResult.CurrencyMismatch ->
                    OperationActionUiState.Error(OperationActionError.CurrencyMismatch)

                OperationActionResult.DuplicateOperation ->
                    OperationActionUiState.Error(OperationActionError.DuplicateOperation)

                OperationActionResult.OperationNotFound ->
                    OperationActionUiState.Error(OperationActionError.OperationNotFound)

                is OperationActionResult.ValidationError ->
                    OperationActionUiState.Error(OperationActionError.InvalidData)

                is OperationActionResult.Error ->
                    OperationActionUiState.Error(OperationActionError.Unexpected)
            }
        }
    }
}
