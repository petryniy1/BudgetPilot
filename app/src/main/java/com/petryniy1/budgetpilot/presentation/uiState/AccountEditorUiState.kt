package com.petryniy1.budgetpilot.presentation.uiState

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import com.petryniy1.budgetpilot.domain.models.AccountType
import com.petryniy1.budgetpilot.domain.models.CurrencyCode

data class AccountEditorUiState(
    val accountId: Int? = null,
    val name: String = "",
    val accountType: AccountType = AccountType.CASH,
    val currency: CurrencyCode = CurrencyCode.PLN,
    val balanceInput: String = "",
    val nameError: String? = null,
    val balanceError: String? = null
)

val AccountEditorUiStateSaver: Saver<AccountEditorUiState?, Any> =
    listSaver(
        save = { state ->
            if (state == null) {
                listOf(false)
            } else {
                listOf(
                    true,
                    state.accountId ?: -1,
                    state.name,
                    state.accountType.name,
                    state.currency.name,
                    state.balanceInput,
                    state.nameError.orEmpty(),
                    state.balanceError.orEmpty()
                )
            }
        },
        restore = { values ->
            val hasState = values[0] as Boolean

            if (!hasState) {
                null
            } else {
                val accountId = values[1] as Int
                val nameError = values[6] as String
                val balanceError = values[7] as String

                AccountEditorUiState(
                    accountId = accountId.takeIf { it != -1 },
                    name = values[2] as String,
                    accountType = AccountType.valueOf(values[3] as String),
                    currency = CurrencyCode.valueOf(values[4] as String),
                    balanceInput = values[5] as String,
                    nameError = nameError.ifBlank { null },
                    balanceError = balanceError.ifBlank { null }
                )
            }
        }
    )
