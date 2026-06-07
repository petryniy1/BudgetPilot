package com.petryniy1.budgetpilot.data.storage.dao

import androidx.room.Dao
import androidx.room.Update
import com.petryniy1.budgetpilot.data.storage.models.AccountEntity

@Dao
interface AccountDao {
    @Update
    suspend fun updateAccount(account: AccountEntity)

}
