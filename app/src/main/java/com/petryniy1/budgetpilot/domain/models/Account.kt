package com.petryniy1.budgetpilot.domain.models

data class Account(
    val id: Int,
    val name: String,
    val type: AccountType,
    val balance: Money
)