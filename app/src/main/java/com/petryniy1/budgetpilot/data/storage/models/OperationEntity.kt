package com.petryniy1.budgetpilot.data.storage.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "operations")
data class OperationEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "moneyHolderId")
    val moneyHolderId: Int,
    @ColumnInfo(name = "value")
    val value: Long,
    @ColumnInfo(name = "categoryDrawable")
    val categoryDrawable: Int,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "comment")
    val comment: String,
)