package com.petryniy1.budgetpilot.data.storage.models

data class BalanceByCurrency(
    val currencyCode: String,
    val amountMinor: Long
)