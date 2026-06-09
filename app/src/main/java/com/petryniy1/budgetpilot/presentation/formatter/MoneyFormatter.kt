package com.petryniy1.budgetpilot.presentation.formatter

import com.petryniy1.budgetpilot.domain.models.Money
import java.math.BigDecimal
import java.math.RoundingMode

fun Money.formatForDisplay(): String {
    val divisor = BigDecimal.TEN.pow(currency.fractionDigits)

    val majorAmount = BigDecimal(amountMinor)
        .divide(divisor)
        .setScale(currency.fractionDigits, RoundingMode.UNNECESSARY)

    return "$majorAmount ${currency.name}"
}