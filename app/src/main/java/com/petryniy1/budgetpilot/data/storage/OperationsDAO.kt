package com.petryniy1.budgetpilot.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.petryniy1.budgetpilot.data.storage.models.OperationEntity
import com.petryniy1.budgetpilot.data.storage.models.OperationWithMoneyHolderEntity
import androidx.room.Transaction

@Dao
interface OperationsDAO {
    @Transaction
    @Query("SELECT * FROM operations ")
    fun getOperations(): Flow<List<OperationWithMoneyHolderEntity>>

    @Transaction
    @Query("SELECT * FROM operations WHERE id = :id")
    fun getOperationById(id: Int): Flow<OperationWithMoneyHolderEntity>

    @Insert
    suspend fun addOperations(operationEntity: OperationEntity)

    @Update
    suspend fun updateOperation(operationEntity: OperationEntity)

    @Query("DELETE FROM operations WHERE id = :id")
    suspend fun deleteOperations(id: Int)

    @Query("SELECT COALESCE(SUM(value), 0) FROM operations")
    fun getOperationsSumValue(): Flow<Long>
}