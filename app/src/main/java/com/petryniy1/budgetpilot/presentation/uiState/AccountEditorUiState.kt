package com.petryniy1.budgetpilot.presentation.uiState

import com.petryniy1.budgetpilot.domain.models.AccountType
import com.petryniy1.budgetpilot.domain.models.CurrencyCode

data class AccountEditorUiState(
    val accountId: Int? = null,
    val name: String = "",
    val accountType: AccountType = AccountType.CASH,
    val currency: CurrencyCode = CurrencyCode.PLN,
    val balanceInput: String = ""
)
