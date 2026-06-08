package com.petryniy1.budgetpilot.presentation.view.v1

import androidx.compose.runtime.Composable
import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.presentation.uiState.OperationActionUiState

@Composable
fun BudgetOperationsScreen(
    operations: List<BudgetOperation>,
    actionState: OperationActionUiState,
    onAddOperationClick: () -> Unit,
    onOperationClick: (BudgetOperation) -> Unit,
    onDeleteOperationClick: (Int) -> Unit
) {

}
