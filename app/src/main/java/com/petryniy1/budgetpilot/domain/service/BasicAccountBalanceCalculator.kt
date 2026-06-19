package com.petryniy1.budgetpilot.domain.service

import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.domain.models.Money
import com.petryniy1.budgetpilot.domain.models.OperationType

class BasicAccountBalanceCalculator :
    AccountBalanceCalculator {
    override fun calculateAfterOperation(
        account: Account,
        operation: BudgetOperation
    ): Money {
        val balanceAmount = account.balance.amountMinor
        val operationAmount = operation.amount.amountMinor

        val newAmount = when (operation.type) {
            OperationType.EXPENSE -> balanceAmount - operationAmount
            OperationType.INCOME -> balanceAmount + operationAmount
            OperationType.TRANSFER -> balanceAmount - operationAmount
        }

        return Money(
            amountMinor = newAmount,
            currency = account.balance.currency
        )
    }

    override fun calculateAfterOperationUpdate(
        account: Account,
        oldOperation: BudgetOperation,
        newOperation: BudgetOperation
    ): Money {
        val balanceAfterRollback = calculateAfterOperationDelete(
            account = account,
            operation = oldOperation
        )

        val accountAfterRollback = account.copy(balance = balanceAfterRollback)

        return calculateAfterOperation(
            account = accountAfterRollback,
            operation = newOperation
        )
    }

    override fun calculateAfterOperationDelete(
        account: Account,
        operation: BudgetOperation
    ): Money {
        val balanceAmount = account.balance.amountMinor
        val operationAmount = operation.amount.amountMinor

        val newAmount = when (operation.type) {
            OperationType.EXPENSE -> balanceAmount + operationAmount
            OperationType.INCOME -> balanceAmount - operationAmount
            OperationType.TRANSFER -> balanceAmount + operationAmount
        }

        return Money(
            amountMinor = newAmount,
            currency = account.balance.currency
        )
    }
}
