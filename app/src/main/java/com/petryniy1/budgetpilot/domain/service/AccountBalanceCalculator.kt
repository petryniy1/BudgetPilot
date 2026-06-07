package com.petryniy1.budgetpilot.domain.service

import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.domain.models.Money

interface AccountBalanceCalculator {
    fun calculateAfterOperation(
        account: Account,
        operation: BudgetOperation
    ): Money

    fun calculateAfterOperationUpdate(
        account: Account,
        oldOperation: BudgetOperation,
        newOperation: BudgetOperation
    ): Money

    fun calculateAfterOperationDelete(
        account: Account,
        operation: BudgetOperation
    ): Money
}
