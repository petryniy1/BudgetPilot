package com.petryniy1.budgetpilot.data

import com.petryniy1.budgetpilot.data.storage.models.MoneyHolderEntity
import com.petryniy1.budgetpilot.data.storage.models.OperationEntity
import com.petryniy1.budgetpilot.data.storage.models.OperationWithMoneyHolderEntity
import com.petryniy1.budgetpilot.domain.models.MoneyHolder
import com.petryniy1.budgetpilot.domain.models.Operation
import com.petryniy1.budgetpilot.domain.models.OperationWithMoneyHolder

fun Operation.toOperationEntity() =
    OperationEntity(
        id = id,
        category = category,
        moneyHolderId = moneyHolderId,
        value = value,
        categoryDrawable = categoryDrawable,
        date = date,
        comment = comment
    )

fun MoneyHolderEntity.toMoneyHolder() =
    MoneyHolder(
        id = moneyId!!,
        name = name!!,
        type = type,
        balance = balance!!
    )

fun MoneyHolder.toMoneyHolderEntity() =
    MoneyHolderEntity(
        moneyId = if (id == 0) null else id,
        name = name,
        type = type,
        balance = balance
    )

fun OperationWithMoneyHolderEntity.toOperationWithMoneyHolder() =
    OperationWithMoneyHolder(
        operationEntity = operationEntity,
        moneyHolderEntity = moneyHolderEntity
    )




