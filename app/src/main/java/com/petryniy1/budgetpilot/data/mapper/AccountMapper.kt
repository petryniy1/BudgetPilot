package com.petryniy1.budgetpilot.data.mapper

import com.petryniy1.budgetpilot.data.storage.models.AccountEntity
import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.AccountType
import com.petryniy1.budgetpilot.domain.models.CurrencyCode
import com.petryniy1.budgetpilot.domain.models.Money

fun AccountEntity.toAccount(): Account {
    return Account(
        id = id,
        name = name,
        type = AccountType.valueOf(type),
        balance = Money(
            amountMinor = balanceMinor,
            currency = CurrencyCode.valueOf(currencyCode)
        )
    )
}

fun Account.toAccountEntity(): AccountEntity {
    return AccountEntity(
        id = id,
        name = name,
        type = type.name,
        balanceMinor = balance.amountMinor,
        currencyCode = balance.currency.name
    )
}