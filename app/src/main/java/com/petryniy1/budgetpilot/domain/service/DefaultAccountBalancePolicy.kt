package com.petryniy1.budgetpilot.domain.service

import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.Money

class DefaultAccountBalancePolicy : AccountBalancePolicy {
    override fun canWithdraw(
        account: Account,
        amount: Money
    ): Boolean {
        if (account.balance.currency != amount.currency) {
            return false
        }
        return account.balance.amountMinor >= amount.amountMinor
    }
}