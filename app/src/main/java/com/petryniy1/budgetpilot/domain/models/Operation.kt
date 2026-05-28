package com.petryniy1.budgetpilot.domain.models

data class Operation(
    var id: Int = 0,
    val category: String,
    val moneyHolderId: Int,
    val value: Long,
    val categoryDrawable: Int,
    val date: String,
    val comment: String,
)

