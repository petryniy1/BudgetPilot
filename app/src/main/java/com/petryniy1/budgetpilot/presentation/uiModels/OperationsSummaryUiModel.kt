package com.petryniy1.budgetpilot.presentation.uiModels

data class OperationsSummaryUiModel(
    val incomeTotals: List<OperationCurrencyTotalUiModel>,
    val expenseTotals: List<OperationCurrencyTotalUiModel>
)

data class OperationCurrencyTotalUiModel(
    val currencyCode: String,
    val amountText: String
)