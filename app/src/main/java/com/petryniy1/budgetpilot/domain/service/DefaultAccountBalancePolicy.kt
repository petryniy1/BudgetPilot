package com.petryniy1.budgetpilot.domain.service

import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.domain.models.OperationType

class DefaultAccountBalancePolicy : AccountBalancePolicy {
    override fun canApplyOperation(
        account: Account,
        operation: BudgetOperation
    ): Boolean {
        if (account.balance.currency != operation.amount.currency) {
            return false
        }
        return when (operation.type) {
            OperationType.EXPENSE,
            OperationType.TRANSFER -> account.balance.amountMinor >=
                    operation.amount.amountMinor

            OperationType.INCOME -> true
        }
    }
}