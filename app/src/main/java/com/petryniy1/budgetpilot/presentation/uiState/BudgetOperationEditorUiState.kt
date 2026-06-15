package com.petryniy1.budgetpilot.presentation.uiState

import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.OperationType
import java.time.LocalDate

data class BudgetOperationEditorUiState(
    val operationId: Int? = null,
    val title: String = "",
    val amountInput: String = "",
    val selectedAccountId: Int? = null,
    val selectedType: OperationType = OperationType.EXPENSE,
    val selectedDate: LocalDate = LocalDate.now(),
    val comment: String = "",
    val availableAccounts: List<Account> = emptyList(),
    val titleError: String? = null,
    val amountError: String? = null,
    val accountError: String? = null
)
