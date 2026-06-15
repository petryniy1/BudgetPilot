package com.petryniy1.budgetpilot.presentation.mapper

import com.petryniy1.budgetpilot.R
import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.AccountType
import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.presentation.uiModels.BudgetOperationListItemUiModel

fun BudgetOperation.toListItemUiModel(
    accounts: List<Account>
): BudgetOperationListItemUiModel {
    val account = accounts.firstOrNull { account ->
        account.id == accountId
    }

    val accountName = account?.name ?: "Unknown account"
    val accountCurrencyCode = account?.balance?.currency?.name ?: amount.currency.name
    val accountType = account?.type ?: AccountType.BANK_ACCOUNT

    return BudgetOperationListItemUiModel(
        id = id,
        title = title,
        categoryName = "Uncategorized",
        categoryIconRes = R.drawable.ic_products,
        accountName = accountName,
        accountCurrencyCode = accountCurrencyCode,
        accountType = accountType,
        amount = amount,
        type = type,
        date = date
    )
}