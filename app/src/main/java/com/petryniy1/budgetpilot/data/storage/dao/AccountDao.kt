package com.petryniy1.budgetpilot.data.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.petryniy1.budgetpilot.data.storage.models.AccountEntity
import com.petryniy1.budgetpilot.data.storage.models.BalanceByCurrency
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts ORDER BY name ASC")
    fun observeAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE id = :id")
    fun observeAccount(id: Int): Flow<AccountEntity?>

    @Query(
        "SELECT currency_code AS currencyCode, " +
                "SUM(balance_minor) AS amountMinor " +
                "FROM accounts " +
                "GROUP BY currency_code"
    )
    fun observeBalancesByCurrency(): Flow<List<BalanceByCurrency>>

    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun findAccount(id: Int): AccountEntity?

    @Insert
    suspend fun insertAccount(account: AccountEntity)

    @Update
    suspend fun updateAccount(account: AccountEntity)

    @Query("DELETE FROM accounts WHERE id = :id")
    suspend fun deleteAccount(id: Int)

    @Query(
        "UPDATE accounts " +
                "SET balance_minor = :balanceMinor, " +
                "currency_code = :currencyCode " +
                "WHERE id = :accountId"
    )
    suspend fun updateBalance(
        accountId: Int,
        balanceMinor: Long,
        currencyCode: String
    )
}
