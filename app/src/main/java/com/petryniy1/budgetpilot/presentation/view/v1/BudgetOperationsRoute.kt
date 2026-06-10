package com.petryniy1.budgetpilot.presentation.view.v1

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.petryniy1.budgetpilot.presentation.viewModels.v1.BudgetOperationsViewModel

@Composable
fun BudgetOperationsRoute(
    viewModel: BudgetOperationsViewModel = hiltViewModel()
) {
    val operations = viewModel.operations.collectAsStateWithLifecycle()
    val actionState = viewModel.operationActionState.collectAsStateWithLifecycle()

    BudgetOperationsScreen(
        operations = operations.value,
        actionState = actionState.value,
        onAddOperationClick = { // TODO Open add operation screen.
        },
        onOperationClick = { _ ->
            // TODO Open operation details screen.
        },
        onDeleteOperationClick = { operationId ->
            viewModel.deleteOperation(operationId)
        }
    )
}