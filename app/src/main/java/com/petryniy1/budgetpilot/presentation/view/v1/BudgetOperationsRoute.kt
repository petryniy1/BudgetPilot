package com.petryniy1.budgetpilot.presentation.view.v1

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.petryniy1.budgetpilot.presentation.uiState.BudgetOperationEditorUiState
import com.petryniy1.budgetpilot.presentation.viewModels.v1.BudgetOperationsViewModel

@Composable
fun BudgetOperationsRoute(
    viewModel: BudgetOperationsViewModel = hiltViewModel()
) {
    val operations = viewModel.operations.collectAsStateWithLifecycle()
    val accounts = viewModel.accounts.collectAsStateWithLifecycle()
    val actionState = viewModel.operationActionState.collectAsStateWithLifecycle()

    var editorState by remember {
        mutableStateOf<BudgetOperationEditorUiState?>(null)
    }

    fun updateEditorState(
        transform: (BudgetOperationEditorUiState) -> BudgetOperationEditorUiState
    ) {
        editorState = editorState?.let(transform)
    }

    BudgetOperationsScreen(
        operations = operations.value,
        actionState = actionState.value,
        onAddOperationClick = {
            editorState = BudgetOperationEditorUiState(
                availableAccounts = accounts.value,
                selectedAccountId = accounts.value.firstOrNull()?.id
            )
        },
        onOperationClick = { _ ->
            // TODO Open operation details screen.
        },
        onDeleteOperationClick = { operationId ->
            viewModel.deleteOperation(operationId)
        }
    )

    editorState?.let { state ->
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
            onCommentChange = { comment ->
                updateEditorState { current ->
                    current.copy(
                        comment = comment
                    )
                }
            },
            onSave = {
                // TODO Convert editor state to BudgetOperation and save.
            },
            onDismiss = {
                editorState = null
            }
        )
    }
}