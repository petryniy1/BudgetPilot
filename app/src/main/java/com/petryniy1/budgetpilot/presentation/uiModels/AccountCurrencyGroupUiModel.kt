package com.petryniy1.budgetpilot.presentation.uiModels

import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.CurrencyCode
import com.petryniy1.budgetpilot.domain.models.Money

data class AccountCurrencyGroupUiModel(
    val currency: CurrencyCode,
    val totalBalance: Money,
    val accounts: List<Account>
)
