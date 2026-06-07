package com.petryniy1.budgetpilot.domain.service

import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.Money

interface AccountBalancePolicy {
    fun canWithdraw(account: Account, amount: Money): Boolean
}