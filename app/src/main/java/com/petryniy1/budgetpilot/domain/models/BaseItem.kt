package com.petryniy1.budgetpilot.domain.models

import com.petryniy1.budgetpilot.data.storage.models.MoneyHolderEntity
import com.petryniy1.budgetpilot.data.storage.models.OperationEntity

sealed class BaseItem

data class HeadItem(
    val date: String
): BaseItem()

data class OperationWithMoneyHolder(
    val operationEntity: OperationEntity,
    val moneyHolderEntity: MoneyHolderEntity
): BaseItem()