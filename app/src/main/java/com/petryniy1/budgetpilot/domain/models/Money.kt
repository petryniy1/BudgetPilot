package com.petryniy1.budgetpilot.domain.models

data class Money(
    val amountMinor: Long,
    val currency: CurrencyCode
)