package com.petryniy1.budgetpilot.presentation.feature.operations

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.petryniy1.budgetpilot.presentation.design.components.BudgetPilotSnackbar
import com.petryniy1.budgetpilot.presentation.mapper.toBudgetOperationOrNull
import com.petryniy1.budgetpilot.presentation.mapper.toEditorUiState
import com.petryniy1.budgetpilot.presentation.mapper.withValidationErrors
import com.petryniy1.budgetpilot.presentation.uiState.BudgetOperationEditorUiState
import com.petryniy1.budgetpilot.presentation.uiState.BudgetOperationEditorUiStateSaver
import com.petryniy1.budgetpilot.presentation.uiState.OperationActionError
import com.petryniy1.budgetpilot.presentation.uiState.OperationActionUiState

@Composable
fun BudgetOperationsRoute(
    viewModel: BudgetOperationsViewModel = hiltViewModel()
) {
    val operations = viewModel.operations.collectAsStateWithLifecycle()
    val accounts = viewModel.accounts.collectAsStateWithLifecycle()
    val actionState = viewModel.operationActionState.collectAsStateWithLifecycle()

    var editorState by rememberSaveable(
        stateSaver = BudgetOperationEditorUiStateSaver
    ) {
        mutableStateOf<BudgetOperationEditorUiState?>(null)
    }

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    fun updateEditorState(
        transform: (BudgetOperationEditorUiState) -> BudgetOperationEditorUiState
    ) {
        editorState = editorState?.let(transform)
    }

    LaunchedEffect(actionState.value) {
        when (val state = actionState.value) {
            OperationActionUiState.Success -> {
                snackbarHostState.showSnackbar("Operation saved")
                viewModel.clearOperationActionState()
            }

            is OperationActionUiState.Error -> {
                snackbarHostState.showSnackbar(state.reason.toMessage())
                viewModel.clearOperationActionState()
            }

            OperationActionUiState.Ready,
            OperationActionUiState.Loading -> Unit
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BudgetOperationsScreen(
            operations = operations.value,
            accounts = accounts.value,
            onAddOperationClick = {
                editorState = BudgetOperationEditorUiState(
                    availableAccounts = accounts.value,
                    selectedAccountId = accounts.value.firstOrNull()?.id
                )
            },
            onOperationClick = { operationId ->
                val operation = operations.value.firstOrNull { operation ->
                    operation.id == operationId
                } ?: return@BudgetOperationsScreen

                editorState = operation.toEditorUiState(
                    availableAccounts = accounts.value
                )
            },
            onDeleteOperationClick = { operationId ->
                viewModel.deleteOperation(operationId)
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

    val editorStateWithAccounts = editorState?.copy(
        availableAccounts = accounts.value
    )

    editorStateWithAccounts?.let { state ->
        BudgetOperationEditorDialog(
            state = state,
            onTitleChange = { title ->
                updateEditorState { current ->
                    current.copy(
                        title = title,
                        titleError = null
                    )
                }
            },
            onAmountChange = { amountInput ->
                updateEditorState { current ->
                    current.copy(
                        amountInput = amountInput,
                        amountError = null
                    )
                }
            },
            onAccountChange = { accountId ->
                updateEditorState { current ->
                    current.copy(
                        selectedAccountId = accountId,
                        accountError = null
                    )
                }
            },
            onTypeChange = { operationType ->
                updateEditorState { current ->
                    current.copy(
                        selectedType = operationType
                    )
                }
            },
            onDateChange = { date ->
                updateEditorState { current ->
                    current.copy(
                        selectedDate = date
                    )
                }
            },
            onCommentChange = { comment ->
                updateEditorState { current ->
                    current.copy(
                        comment = comment
                    )
                }
            },
            onSave = {
                val currentState = editorStateWithAccounts ?: return@BudgetOperationEditorDialog
                val validatedState = currentState.withValidationErrors()

                editorState = validatedState

                val operation = validatedState.toBudgetOperationOrNull()
                    ?: return@BudgetOperationEditorDialog

                if (validatedState.operationId == null) {
                    viewModel.addOperation(operation)
                } else {
                    viewModel.updateOperation(operation)
                }

                editorState = null
            },
            onDismiss = {
                editorState = null
            }
        )
    }
}

private fun OperationActionError.toMessage(): String {
    return when (this) {
        OperationActionError.AccountNotFound -> "Account not found"
        OperationActionError.InsufficientFunds -> "Insufficient funds"
        OperationActionError.CurrencyMismatch -> "Currency mismatch"
        OperationActionError.DuplicateOperation -> "Duplicate operation"
        OperationActionError.OperationNotFound -> "Operation not found"
        OperationActionError.InvalidData -> "Invalid operation data"
        OperationActionError.Unexpected -> "Unexpected error"
    }
}
