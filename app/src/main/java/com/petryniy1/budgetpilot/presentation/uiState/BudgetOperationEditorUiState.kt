package com.petryniy1.budgetpilot.presentation.uiState

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
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

val BudgetOperationEditorUiStateSaver: Saver<BudgetOperationEditorUiState?, Any> =
    listSaver(
        save = { state ->
            if (state == null) {
                listOf(false)
            } else {
                listOf(
                    true,
                    state.operationId ?: -1,
                    state.title,
                    state.amountInput,
                    state.selectedAccountId ?: -1,
                    state.selectedType.name,
                    state.selectedDate.toEpochDay(),
                    state.comment,
                    state.titleError.orEmpty(),
                    state.amountError.orEmpty(),
                    state.accountError.orEmpty()
                )
            }
        },
        restore = { values ->
            val hasState = values[0] as Boolean

            if (!hasState) {
                null
            } else {
                val operationId = values[1] as Int
                val selectedAccountId = values[4] as Int
                val titleError = values[8] as String
                val amountError = values[9] as String
                val accountError = values[10] as String

                BudgetOperationEditorUiState(
                    operationId = operationId.takeIf { it != -1 },
                    title = values[2] as String,
                    amountInput = values[3] as String,
                    selectedAccountId = selectedAccountId.takeIf { it != -1 },
                    selectedType = OperationType.valueOf(values[5] as String),
                    selectedDate = LocalDate.ofEpochDay(values[6] as Long),
                    comment = values[7] as String,
                    availableAccounts = emptyList(),
                    titleError = titleError.ifBlank { null },
                    amountError = amountError.ifBlank { null },
                    accountError = accountError.ifBlank { null }
                )
            }
        }
    )