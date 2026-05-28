package com.petryniy1.budgetpilot.data.storage.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moneyHolder")
data class MoneyHolderEntity(
    @PrimaryKey(autoGenerate = true)
    var moneyId: Int?,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "type")
    val type: Int?,
    @ColumnInfo(name = "balance")
    val balance: Long?
)
