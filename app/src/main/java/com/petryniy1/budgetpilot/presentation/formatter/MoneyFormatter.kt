package com.petryniy1.budgetpilot.presentation.formatter

import com.petryniy1.budgetpilot.domain.models.Money
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun Money.formatForDisplay(): String {
    val divisor = BigDecimal.TEN.pow(currency.fractionDigits)

    val majorAmount = BigDecimal(amountMinor)
        .divide(divisor)
        .setScale(currency.fractionDigits, RoundingMode.UNNECESSARY)

    val symbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }

    val pattern = buildString {
        append("#,##0")
        if (currency.fractionDigits > 0) {
            append(".")
            repeat(currency.fractionDigits) {
                append("0")
            }
        }
    }

    val formatter = DecimalFormat(pattern, symbols).apply {
        isGroupingUsed = true
    }

    return "${formatter.format(majorAmount)} ${currency.name}"
}