package com.petryniy1.budgetpilot.domain.repository

import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.Money
import com.petryniy1.budgetpilot.domain.models.results.AccountActionResult
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun observeAccounts(): Flow<List<Account>>

    fun observeAccount(id: Int): Flow<Account?>

    fun observeBalancesByCurrency(): Flow<List<Money>>

    suspend fun findAccount(id: Int): Account?

    suspend fun createAccount(account: Account): AccountActionResult

    suspend fun updateAccount(account: Account): AccountActionResult

    suspend fun deleteAccount(id: Int): AccountActionResult

    suspend fun updateBalance(accountId: Int, balance: Money): AccountActionResult
}