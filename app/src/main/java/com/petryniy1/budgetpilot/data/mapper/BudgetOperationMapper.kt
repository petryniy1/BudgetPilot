package com.petryniy1.budgetpilot.data.mapper

import com.petryniy1.budgetpilot.data.storage.models.BudgetOperationEntity
import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.domain.models.CurrencyCode
import com.petryniy1.budgetpilot.domain.models.Money
import com.petryniy1.budgetpilot.domain.models.OperationType
import java.time.LocalDate

fun BudgetOperationEntity.toBudgetOperation(): BudgetOperation {
    return BudgetOperation(
        id = id,
        accountId = accountId,
        title = title,
        amount = Money(
            amountMinor = amountMinor,
            currency = CurrencyCode.valueOf(currencyCode)
        ),
        type = OperationType.valueOf(type),
        date = LocalDate.ofEpochDay(dateEpochDay),
        comment = comment
    )
}

fun BudgetOperation.toBudgetOperationEntity(): BudgetOperationEntity {
    return BudgetOperationEntity(
        id = id,
        accountId = accountId,
        title = title,
        amountMinor = amount.amountMinor,
        currencyCode = amount.currency.name,
        type = type.name,
        dateEpochDay = date.toEpochDay(),
        comment = comment
    )
}