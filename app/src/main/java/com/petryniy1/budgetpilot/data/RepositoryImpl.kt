package com.petryniy1.budgetpilot.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import com.petryniy1.budgetpilot.data.storage.MoneyHolderDao
import com.petryniy1.budgetpilot.data.storage.OperationsDAO
import com.petryniy1.budgetpilot.domain.models.MoneyHolder
import com.petryniy1.budgetpilot.domain.models.Operation
import com.petryniy1.budgetpilot.domain.models.OperationWithMoneyHolder
import com.petryniy1.budgetpilot.domain.repository.MoneyHoldersRepository
import com.petryniy1.budgetpilot.domain.repository.OperationsRepository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(

    private val operationsDAO: OperationsDAO,
    private val moneyHolderDao: MoneyHolderDao

) :
    MoneyHoldersRepository, OperationsRepository {

    override fun getAllOperations(): Flow<List<OperationWithMoneyHolder?>> {
        return operationsDAO.getOperations().map {
            it.map { operationWithMoneyHolderEntity ->
                operationWithMoneyHolderEntity.toOperationWithMoneyHolder()
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getOperationById(id: Int): Flow<OperationWithMoneyHolder?> {
        return operationsDAO.getOperationById(id).map { it.toOperationWithMoneyHolder() }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun addOperation(operation: Operation) {
        withContext(Dispatchers.IO) {
            operationsDAO.addOperations(operation.toOperationEntity())
        }
    }

    override suspend fun updateOperation(operation: Operation) {
        withContext(Dispatchers.IO) {
            operationsDAO.updateOperation(operation.toOperationEntity())
        }
    }

    override suspend fun deleteOperation(id: Int) {
        withContext(Dispatchers.IO) {
            operationsDAO.deleteOperations(id)
        }
    }

    override fun getOperationsSumValue(): Flow<Long?> {
        return operationsDAO.getOperationsSumValue().flowOn(Dispatchers.IO)
    }

    override fun getMoneyHolders(): Flow<List<MoneyHolder?>> {
        return moneyHolderDao.getAccounts().map {
            it.map { moneyHolderEntity ->
                moneyHolderEntity.toMoneyHolder()
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getMoneyHolderById(id: Int): MoneyHolder? {
        return withContext(Dispatchers.IO) {
            moneyHolderDao.getAccountById(id).toMoneyHolder()
        }
    }

    override suspend fun addMoneyHolder(moneyHolder: MoneyHolder) {
        withContext(Dispatchers.IO) {
            moneyHolderDao.addAccount(moneyHolder.toMoneyHolderEntity())
        }
    }

    override suspend fun updateMoneyHolder(moneyHolder: MoneyHolder) {
        withContext(Dispatchers.IO) {
            moneyHolderDao.updateAccount(moneyHolder.toMoneyHolderEntity())
        }
    }

    override suspend fun deleteMoneyHolder(id: Int) {
        withContext(Dispatchers.IO) {
            moneyHolderDao.deleteAccount(id)
        }
    }

    override fun getMoneyHoldersSumBalance(): Flow<Long?> {
        return moneyHolderDao.getAccountTotalBalance().flowOn(Dispatchers.IO)
    }

}