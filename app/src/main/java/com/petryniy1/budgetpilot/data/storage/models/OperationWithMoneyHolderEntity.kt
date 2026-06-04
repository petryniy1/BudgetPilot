package com.petryniy1.budgetpilot.data.storage.models

import androidx.room.Embedded
import androidx.room.Relation

data class OperationWithMoneyHolderEntity(
    @Embedded
    val operationEntity: OperationEntity,

    @Relation(
        parentColumn = "account_id",
        entityColumn = "id"
    )
    val accountEntity: AccountEntity
)
