package com.petryniy1.budgetpilot.presentation.formatter

import com.petryniy1.budgetpilot.domain.models.OperationType

fun OperationType.formatForDisplay(): String {
    return when (this) {
        OperationType.EXPENSE -> "Expense"
        OperationType.INCOME -> "Income"
        OperationType.TRANSFER -> "Transfer"
    }
}