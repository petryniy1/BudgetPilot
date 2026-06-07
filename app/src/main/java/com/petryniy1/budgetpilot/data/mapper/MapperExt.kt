package com.petryniy1.budgetpilot.data.mapper

import com.petryniy1.budgetpilot.data.storage.models.AccountEntity
import com.petryniy1.budgetpilot.data.storage.models.OperationEntity
import com.petryniy1.budgetpilot.data.storage.models.OperationWithMoneyHolderEntity
import com.petryniy1.budgetpilot.domain.models.MoneyHolder
import com.petryniy1.budgetpilot.domain.models.Operation
import com.petryniy1.budgetpilot.domain.models.OperationWithMoneyHolder

fun AccountEntity.toMoneyHolder() =
    MoneyHolder(
        id = id,
        name = name,
        type = type.toIntOrNull() ?: 0,
        balance = balanceMinor
    )

fun MoneyHolder.toMoneyHolderEntity() =
    AccountEntity(
        id = id,
        name = name,
        type = type.toString(),
        balanceMinor = balance,
        currencyCode = "PLN"
    )

fun Operation.toOperationEntity() =
    OperationEntity(
        id = id,
        category = category,
        accountId = moneyHolderId,
        value = value,
        categoryDrawable = categoryDrawable,
        date = date,
        comment = comment
    )

fun OperationWithMoneyHolderEntity.toOperationWithMoneyHolder() =
    OperationWithMoneyHolder(
        operationEntity = operationEntity,
        accountEntity = accountEntity
    )




