package com.petryniy1.budgetpilot.data.repository

import androidx.room.withTransaction
import com.petryniy1.budgetpilot.data.mapper.toAccountEntity
import com.petryniy1.budgetpilot.data.mapper.toBudgetOperation
import com.petryniy1.budgetpilot.data.mapper.toBudgetOperationEntity
import com.petryniy1.budgetpilot.data.storage.AppDatabase
import com.petryniy1.budgetpilot.data.storage.dao.AccountDao
import com.petryniy1.budgetpilot.data.storage.dao.BudgetOperationDao
import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.domain.models.results.OperationActionResult
import com.petryniy1.budgetpilot.domain.repository.BudgetOperationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BudgetOperationRepositoryImpl @Inject constructor(
    private val budgetOperationDao: BudgetOperationDao,
    private val accountDao: AccountDao,
    private val appDatabase: AppDatabase
) : BudgetOperationRepository {
    override fun observeOperations(): Flow<List<BudgetOperation>> {
        return budgetOperationDao.observeOperations()
            .map { operations ->
                operations.map { operationEntity ->
                    operationEntity.toBudgetOperation()
                }
            }
    }

    override fun observeOperation(id: Int): Flow<BudgetOperation?> {
        return budgetOperationDao.observeOperation(id)
            .map { operationEntity ->
                operationEntity?.toBudgetOperation()
            }
    }

    override fun observeOperationsByAccount(accountId: Int): Flow<List<BudgetOperation>> {
        return budgetOperationDao.observeOperationsByAccount(accountId)
            .map { operations ->
                operations.map { operationEntity ->
                    operationEntity.toBudgetOperation()
                }
            }
    }

    override suspend fun findOperation(id: Int): BudgetOperation? {
        return budgetOperationDao.findOperation(id)?.toBudgetOperation()
    }

    override suspend fun createOperation(operation: BudgetOperation): OperationActionResult {
        return try {
            budgetOperationDao.insertOperation(operation.toBudgetOperationEntity())
            OperationActionResult.Success
        } catch (throwable: Throwable) {
            OperationActionResult.Error(throwable)
        }
    }

    override suspend fun createOperationAndUpdateAccount(
        operation: BudgetOperation, updatedAccount: Account
    ): OperationActionResult {
        return try {
            appDatabase.withTransaction {
                budgetOperationDao.insertOperation(operation.toBudgetOperationEntity())
                accountDao.updateAccount(updatedAccount.toAccountEntity())
            }
            OperationActionResult.Success
        } catch (throwable: Throwable) {
            OperationActionResult.Error(throwable)
        }
    }

    override suspend fun updateOperationAndUpdateAccount(
        operation: BudgetOperation, updatedAccount: Account
    ): OperationActionResult {
        return try {
            appDatabase.withTransaction {
                budgetOperationDao.updateOperation(operation.toBudgetOperationEntity())
                accountDao.updateAccount(updatedAccount.toAccountEntity())
            }
            OperationActionResult.Success
        } catch (throwable: Throwable) {
            OperationActionResult.Error(throwable)
        }
    }

    override suspend fun deleteOperationAndUpdateAccount(
        operation: BudgetOperation, updatedAccount: Account
    ): OperationActionResult {
        return try {
            appDatabase.withTransaction {
                budgetOperationDao.deleteOperation(operation.id)
                accountDao.updateAccount(updatedAccount.toAccountEntity())
            }
            OperationActionResult.Success
        } catch (throwable: Throwable) {
            OperationActionResult.Error(throwable)
        }
    }
}