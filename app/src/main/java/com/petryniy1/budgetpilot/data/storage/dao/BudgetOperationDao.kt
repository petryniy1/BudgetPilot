package com.petryniy1.budgetpilot.data.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.petryniy1.budgetpilot.data.storage.models.BudgetOperationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetOperationDao {
    @Query(
        "SELECT * FROM operations " +
                "ORDER BY date_epoch_day DESC, id DESC"
    )
    fun observeOperations(): Flow<List<BudgetOperationEntity>>

    @Query("SELECT * FROM operations WHERE id = :id")
    fun observeOperation(id: Int): Flow<BudgetOperationEntity?>

    @Query(
        "SELECT * FROM operations " +
                "WHERE account_id = :accountId " +
                "ORDER BY date_epoch_day DESC, id DESC"
    )
    fun observeOperationsByAccount(accountId: Int): Flow<List<BudgetOperationEntity>>

    @Query("SELECT * FROM operations WHERE id = :id")
    suspend fun findOperation(id: Int): BudgetOperationEntity?

    @Insert
    suspend fun insertOperation(operation: BudgetOperationEntity)

    @Update
    suspend fun updateOperation(operation: BudgetOperationEntity)

    @Query("DELETE FROM operations WHERE id = :id")
    suspend fun deleteOperation(id: Int)
}