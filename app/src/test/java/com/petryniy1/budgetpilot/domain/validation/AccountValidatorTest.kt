package com.petryniy1.budgetpilot.domain.validation

import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.AccountType
import com.petryniy1.budgetpilot.domain.models.CurrencyCode
import com.petryniy1.budgetpilot.domain.models.Money
import com.petryniy1.budgetpilot.domain.results.AccountValidationResult
import org.junit.Assert.assertEquals
import org.junit.Test

class AccountValidatorTest {
    private val validator = AccountValidator()

    @Test
    fun `returns EmptyName when account is blank`() {
        //Arrange
        val account = createAccount(
            name = ""
        )

        //Act
        val result = validator.validate(account)

        //Assert
        assertEquals(
            AccountValidationResult.EmptyName,
            result
        )
    }

    @Test
    fun `returns NegativeBalance when balance is negative`() {
        //Arrange
        val account = createAccount(
            balanceMinor = -1
        )

        //Act
        val result = validator.validate(account)

        //Assert
        assertEquals(
            AccountValidationResult.NegativeBalance,
            result
        )
    }

    @Test
    fun `returns Valid when account data is correct`() {
        //Arrange
        val account = createAccount()

        //Act
        val result = validator.validate(account)

        //Assert
        assertEquals(
            AccountValidationResult.Valid,
            result
        )
    }

    private fun createAccount(
        name: String = "Cash wallet",
        balanceMinor: Long = 100_00
    ): Account {
        return Account(
            id = 1,
            name = name,
            type = AccountType.CASH,
            balance = Money(
                amountMinor = balanceMinor,
                currency = CurrencyCode.PLN
            )
        )
    }
}
