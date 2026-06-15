package com.petryniy1.budgetpilot.presentation.mapper

import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.domain.models.Money
import com.petryniy1.budgetpilot.presentation.uiState.BudgetOperationEditorUiState
import java.math.BigDecimal
import java.math.RoundingMode

fun BudgetOperationEditorUiState.withValidationErrors(): BudgetOperationEditorUiState {
    return copy(
        titleError = if (title.isBlank()) {
            "Title cannot be empty"
        } else {
            null
        },
        amountError = if (amountInput.toMinorAmountOrNull(
                fractionDigits = selectedAccountCurrencyFractionDigits()
            ) == null
        ) {
            "Enter a valid amount"
        } else {
            null
        },
        accountError = if (selectedAccountId == null) {
            "Select an account"
        } else {
            null
        }
    )
}

fun BudgetOperation.toEditorUiState(
    availableAccounts: List<Account>
): BudgetOperationEditorUiState {
    return BudgetOperationEditorUiState(
        operationId = id,
        title = title,
        amountInput = amount.toInputString(),
        selectedAccountId = accountId,
        selectedType = type,
        selectedDate = date,
        comment = comment,
        availableAccounts = availableAccounts
    )
}

fun BudgetOperationEditorUiState.toBudgetOperationOrNull(): BudgetOperation? {
    val accountId = selectedAccountId ?: return null
    val account = availableAccounts.firstOrNull { account ->
        account.id == accountId
    } ?: return null

    val amountMinor = amountInput.toMinorAmountOrNull(
        fractionDigits = account.balance.currency.fractionDigits
    ) ?: return null

    return BudgetOperation(
        id = operationId ?: 0,
        accountId = accountId,
        title = title.trim(),
        amount = Money(
            amountMinor = amountMinor,
            currency = account.balance.currency
        ),
        type = selectedType,
        date = selectedDate,
        comment = comment.trim()
    )
}

private fun BudgetOperationEditorUiState.selectedAccountCurrencyFractionDigits(): Int {
    val accountId = selectedAccountId ?: return 2

    return availableAccounts.firstOrNull { account ->
        account.id == accountId
    }?.balance?.currency?.fractionDigits ?: 2
}

private fun Money.toInputString(): String {
    return BigDecimal(amountMinor)
        .movePointLeft(currency.fractionDigits)
        .setScale(currency.fractionDigits, RoundingMode.UNNECESSARY)
        .toPlainString()
}

private fun String.toMinorAmountOrNull(
    fractionDigits: Int
): Long? {
    val normalized = trim()
        .replace(" ", "")
        .replace(",", ".")

    if (normalized.isBlank()) {
        return null
    }

    return runCatching {
        BigDecimal(normalized)
            .movePointRight(fractionDigits)
            .setScale(0, RoundingMode.HALF_UP)
            .longValueExact()
    }.getOrNull()
}