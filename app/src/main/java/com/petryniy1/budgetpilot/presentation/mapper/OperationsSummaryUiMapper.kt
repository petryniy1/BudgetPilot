package com.petryniy1.budgetpilot.presentation.mapper

import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.domain.models.CurrencyCode
import com.petryniy1.budgetpilot.domain.models.Money
import com.petryniy1.budgetpilot.domain.models.OperationType
import com.petryniy1.budgetpilot.presentation.formatter.formatForDisplay
import com.petryniy1.budgetpilot.presentation.uiModels.OperationCurrencyTotalUiModel
import com.petryniy1.budgetpilot.presentation.uiModels.OperationsSummaryUiModel
import kotlin.math.abs

fun List<BudgetOperation>.toOperationsSummaryUiModel(): OperationsSummaryUiModel {
    return OperationsSummaryUiModel(
        incomeTotals = buildTotalsFor(OperationType.INCOME),
        expenseTotals = buildTotalsFor(OperationType.EXPENSE)
    )
}

private fun List<BudgetOperation>.buildTotalsFor(
    type: OperationType
): List<OperationCurrencyTotalUiModel> {
    return CurrencyCode.entries.map { currency ->
        val totalMinor = filter { operation ->
            operation.type == type && operation.amount.currency == currency
        }.sumOf { operation ->
            abs(operation.amount.amountMinor)
        }

        OperationCurrencyTotalUiModel(
            currencyCode = currency.name,
            amountText = Money(
                amountMinor = totalMinor,
                currency = currency
            ).formatForDisplay()
        )
    }
}