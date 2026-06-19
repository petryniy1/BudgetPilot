package com.petryniy1.budgetpilot.data.storage.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.petryniy1.budgetpilot.data.storage.AppDatabase
import com.petryniy1.budgetpilot.data.storage.models.AccountEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccountDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var accountDao: AccountDao

    @Before
    fun setUp() {
        val context =
            ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        accountDao = database.getAccountDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAccount_observeAccounts_returnsInsertedAccount() = runBlocking {
        // Arrange
        val account1 = createAccount(
            id = 1,
            name = "Cash PLN"
        )

        val account2 = createAccount(
            id = 2,
            name = "Santander"
        )

        val account3 = createAccount(
            id = 3,
            name = "Cash EUR"
        )

        // Act
        accountDao.insertAccount(account1)
        accountDao.insertAccount(account2)
        accountDao.insertAccount(account3)

        val accounts = accountDao
            .observeAccounts()
            .first()

        // Assert
        assertEquals(
            listOf(
                account3, // Cash EUR
                account1, // Cash PLN
                account2  // Santander
            ),
            accounts
        )
    }

    private fun createAccount(
        id: Int,
        name: String,
        balanceMinor: Long = 100_00
    ): AccountEntity {
        return AccountEntity(
            id = id,
            name = name,
            type = "CASH",
            balanceMinor = balanceMinor,
            currencyCode = "PLN"
        )
    }
}
