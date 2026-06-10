package com.petryniy1.budgetpilot.presentation.uiModels

import androidx.annotation.DrawableRes
import com.petryniy1.budgetpilot.domain.models.AccountType
import com.petryniy1.budgetpilot.domain.models.Money
import com.petryniy1.budgetpilot.domain.models.OperationType
import java.time.LocalDate

data class BudgetOperationListItemUiModel(
    val id: Int,
    val title: String,
    val categoryName: String,
    @param:DrawableRes
    val categoryIconRes: Int,
    val accountName: String,
    val accountType: AccountType,
    val amount: Money,
    val type: OperationType,
    val date: LocalDate
)
