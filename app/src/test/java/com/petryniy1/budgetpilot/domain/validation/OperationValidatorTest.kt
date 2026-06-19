package com.petryniy1.budgetpilot.domain.validation

import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.domain.models.CurrencyCode
import com.petryniy1.budgetpilot.domain.models.Money
import com.petryniy1.budgetpilot.domain.models.OperationType
import com.petryniy1.budgetpilot.domain.results.OperationValidationResult
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class OperationValidatorTest {
    private lateinit var validator: OperationValidator

    @Before
    fun setUp() {
        validator = OperationValidator()
    }

    @Test
    fun `returns NonPositiveAmount when amount is zero`() {
        // Arrange
        val operation = createOperation(
            amountMinor = -1
        )

        // Act
        val result = validator.validate(operation)

        // Assert
        assertEquals(
            OperationValidationResult.NonPositiveAmount,
            result
        )
    }

    @Test
    fun `returns Valid when operation data is correct`() {
        // Arrange
        val operation = createOperation()

        // Act
        val result = validator.validate(operation)

        // Assert
        assertEquals(
            OperationValidationResult.Valid,
            result
        )
    }

    @Test
    fun `returns EmptyTitle when operation title is blank`() {
        // Arrange
        val operation = createOperation(
            title = " "
        )

        // Act
        val result = validator.validate(operation)

        // Assert
        assertEquals(
            OperationValidationResult.EmptyTitle,
            result
        )
    }

    private fun createOperation(
        title: String = "Groceries",
        amountMinor: Long = 50_00
    ): BudgetOperation {
        return BudgetOperation(
            id = 1,
            accountId = 1,
            title = title,
            amount = Money(
                amountMinor = amountMinor,
                currency = CurrencyCode.PLN
            ),
            type = OperationType.EXPENSE,
            date = LocalDate.of(2026, 6, 18),
            comment = ""
        )
    }
}
