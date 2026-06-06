package com.petryniy1.budgetpilot.domain.models

import java.time.LocalDate

data class BudgetOperation(
    val id: Int,
    val accountId: Int,
    val title: String,
    val amount: Money,
    val type: OperationType,
    val date: LocalDate,
    val comment: String
)
