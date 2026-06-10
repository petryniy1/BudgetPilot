package com.petryniy1.budgetpilot.presentation.mapper

import com.petryniy1.budgetpilot.R
import com.petryniy1.budgetpilot.domain.models.AccountType
import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.presentation.uiModels.BudgetOperationListItemUiModel

fun BudgetOperation.toListItemUiModel(): BudgetOperationListItemUiModel {
    return BudgetOperationListItemUiModel(
        id = id,
        title = title,
        categoryName = "Uncategorized", // TODO Replace with real category data.
        categoryIconRes = R.drawable.ic_products, // TODO Replace with real category icon.
        accountName = "Account $accountId", // TODO Replace with real account display data.
        accountType = AccountType.BANK_ACCOUNT, // TODO Replace with real account type.
        amount = amount,
        type = type,
        date = date
    )
}