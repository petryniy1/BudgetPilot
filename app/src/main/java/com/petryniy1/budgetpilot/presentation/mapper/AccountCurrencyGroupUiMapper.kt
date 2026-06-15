package com.petryniy1.budgetpilot.presentation.mapper

import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.Money
import com.petryniy1.budgetpilot.presentation.uiModels.AccountCurrencyGroupUiModel

fun List<Account>.toCurrencyGroups(): List<AccountCurrencyGroupUiModel> {
    return groupBy { account ->
        account.balance.currency
    }.map { (currency, accounts) ->
        AccountCurrencyGroupUiModel(
            currency = currency,
            totalBalance = Money(
                amountMinor = accounts.sumOf { account ->
                    account.balance.amountMinor
                },
                currency = currency
            ),
            accounts = accounts.sortedBy { account ->
                account.name
            }
        )
    }.sortedBy { group ->
        group.currency.name
    }
}

