package com.petryniy1.budgetpilot.domain.service

import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.BudgetOperation

interface AccountBalancePolicy {
    fun canApplyOperation(account: Account, operation: BudgetOperation): Boolean
}