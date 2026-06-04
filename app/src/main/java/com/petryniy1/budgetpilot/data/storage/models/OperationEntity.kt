package com.petryniy1.budgetpilot.data.storage.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

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
data class OperationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "account_id")
    val accountId: Int,

    @ColumnInfo(name = "value")
    val value: Long,

    @ColumnInfo(name = "categoryDrawable")
    val categoryDrawable: Int,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "comment")
    val comment: String
)