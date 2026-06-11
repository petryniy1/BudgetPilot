package com.petryniy1.budgetpilot.data.repository

import com.petryniy1.budgetpilot.data.mapper.toAccount
import com.petryniy1.budgetpilot.data.mapper.toAccountEntity
import com.petryniy1.budgetpilot.data.storage.dao.AccountDao
import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.CurrencyCode
import com.petryniy1.budgetpilot.domain.models.Money
import com.petryniy1.budgetpilot.domain.repository.AccountRepository
import com.petryniy1.budgetpilot.domain.results.AccountActionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao
) : AccountRepository {
    override fun observeAccounts(): Flow<List<Account>> {
        return accountDao.observeAccounts()
            .map { accounts ->
                accounts.map { accountEntity ->
                    accountEntity.toAccount()
                }
            }
    }

    override fun observeAccount(id: Int): Flow<Account?> {
        return accountDao.observeAccount(id)
            .map { accountEntity ->
                accountEntity?.toAccount()
            }
    }

    override fun observeBalancesByCurrency(): Flow<List<Money>> {
        return accountDao.observeBalancesByCurrency()
            .map { balances ->
                balances.map { balance ->
                    Money(
                        amountMinor = balance.amountMinor,
                        currency = CurrencyCode.valueOf(balance.currencyCode)
                    )
                }
            }
    }

    override suspend fun findAccount(id: Int): Account? {
        return accountDao.findAccount(id)?.toAccount()
    }

    override suspend fun createAccount(account: Account): AccountActionResult {
        return try {
            accountDao.insertAccount(account.toAccountEntity())
            AccountActionResult.Success
        } catch (throwable: Throwable) {
            AccountActionResult.Error(throwable)
        }
    }

    override suspend fun updateAccount(account: Account): AccountActionResult {
        return try {
            val updatedRows = accountDao
                .updateAccount(account.toAccountEntity())

            if (updatedRows == 0) {
                AccountActionResult.AccountNotFound
            } else {
                AccountActionResult.Success
            }

        } catch (throwable: Throwable) {
            AccountActionResult.Error(throwable)
        }
    }

    override suspend fun deleteAccount(id: Int): AccountActionResult {
        return try {
            val deletedRows = accountDao.deleteAccount(id)

            if (deletedRows == 0) {
                AccountActionResult.AccountNotFound
            } else {
                AccountActionResult.Success
            }

        } catch (throwable: Throwable) {
            AccountActionResult.Error(throwable)
        }
    }

    override suspend fun updateBalance(
        accountId: Int,
        balance: Money
    ): AccountActionResult {
        return try {
            val updatedRows = accountDao.updateBalance(
                accountId = accountId,
                balanceMinor = balance.amountMinor,
                currencyCode = balance.currency.name
            )

            if (updatedRows == 0) {
                AccountActionResult.AccountNotFound
            } else {
                AccountActionResult.Success
            }

        } catch (throwable: Throwable) {
            AccountActionResult.Error(throwable)
        }
    }

    override suspend fun existsAccountWithName(
        name: String,
        excludeId: Int?
    ): Boolean {
        return if (excludeId == null) {
            accountDao.existsAccountWithName(name)
        } else {
            accountDao.existsAccountWithNameExcludingId(
                name = name,
                excludeId = excludeId
            )
        }
    }
}