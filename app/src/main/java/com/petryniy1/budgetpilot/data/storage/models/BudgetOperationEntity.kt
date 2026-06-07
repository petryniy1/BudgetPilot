package com.petryniy1.budgetpilot.data.storage.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "operations",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["account_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["account_id"])
    ]
)
data class BudgetOperationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Int = 0,

    @ColumnInfo("account_id")
    val accountId: Int,

    @ColumnInfo("title")
    val title: String,

    @ColumnInfo("amount_minor")
    val amountMinor: Long,

    @ColumnInfo("currency_code")
    val currencyCode: String,

    @ColumnInfo("type")
    val type: String,

    @ColumnInfo("date_epoch_day")
    val dateEpochDay: Long,

    @ColumnInfo("comment")
    val comment: String
)


