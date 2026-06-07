package com.petryniy1.budgetpilot.data.repository

import com.petryniy1.budgetpilot.data.storage.dao.AccountDao
import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.Money
import com.petryniy1.budgetpilot.domain.models.results.AccountActionResult
import com.petryniy1.budgetpilot.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao
) : AccountRepository {
    override fun observeAccounts(): Flow<List<Account>> {
        TODO("Not yet implemented")
    }

    override fun observeAccount(id: Int): Flow<Account?> {
        TODO("Not yet implemented")
    }

    override fun observeBalancesByCurrency(): Flow<List<Money>> {
        TODO("Not yet implemented")
    }

    override suspend fun findAccount(id: Int): Account? {
        TODO("Not yet implemented")
    }

    override suspend fun createAccount(account: Account): AccountActionResult {
        TODO("Not yet implemented")
    }

    override suspend fun updateAccount(account: Account): AccountActionResult {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccount(id: Int): AccountActionResult {
        TODO("Not yet implemented")
    }

    override suspend fun updateBalance(
        accountId: Int,
        balance: Money
    ): AccountActionResult {
        TODO("Not yet implemented")
    }
}