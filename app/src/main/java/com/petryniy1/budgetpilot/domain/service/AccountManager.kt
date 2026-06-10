package com.petryniy1.budgetpilot.domain.service

import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.results.AccountActionResult

interface AccountManager {
    suspend fun createAccount(account: Account): AccountActionResult

    suspend fun updateAccount(account: Account): AccountActionResult

    suspend fun deleteAccount(id: Int): AccountActionResult
}