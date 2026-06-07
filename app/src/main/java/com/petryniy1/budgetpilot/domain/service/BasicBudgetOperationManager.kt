package com.petryniy1.budgetpilot.domain.service

import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.domain.models.results.OperationActionResult
import com.petryniy1.budgetpilot.domain.models.results.OperationValidationResult
import com.petryniy1.budgetpilot.domain.repository.AccountRepository
import com.petryniy1.budgetpilot.domain.repository.BudgetOperationRepository
import com.petryniy1.budgetpilot.domain.validation.OperationValidator

class BasicBudgetOperationManager(
    private val accountRepository: AccountRepository,
    private val operationRepository: BudgetOperationRepository,
    private val operationValidator: OperationValidator,
    private val balanceCalculator: AccountBalanceCalculator,
    private val accountBalancePolicy: AccountBalancePolicy
) : BudgetOperationManager {
    override suspend fun addOperation(operation: BudgetOperation): OperationActionResult {
        val validationResult = operationValidator.validate(operation)
        if (validationResult != OperationValidationResult.Valid) {
            return OperationActionResult.ValidationError(validationResult)
        }

        val account = accountRepository.findAccount(operation.accountId)
            ?: return OperationActionResult.AccountNotFound
        if (account.balance.currency != operation.amount.currency) {
            return OperationActionResult.CurrencyMismatch
        }
        if (!accountBalancePolicy.canWithdraw(account, operation.amount)) {
            return OperationActionResult.InsufficientFunds
        }

        val updatedBalance = balanceCalculator.calculateAfterOperation(
            account = account, operation = operation
        )

        val updatedAccount = account.copy(balance = updatedBalance)

        return operationRepository.createOperationAndUpdateAccount(
            operation = operation, updatedAccount = updatedAccount
        )
    }

    override suspend fun updateOperation(operation: BudgetOperation): OperationActionResult {
        TODO("Not yet implemented")
    }

    override suspend fun deleteOperation(id: Int): OperationActionResult {
        val operation =
            operationRepository.findOperation(id) ?: return OperationActionResult.OperationNotFound

        val account = accountRepository.findAccount(operation.accountId)
            ?: return OperationActionResult.AccountNotFound
        if (account.balance.currency != operation.amount.currency) {
            return OperationActionResult.CurrencyMismatch
        }

        val updatedBalance = balanceCalculator.calculateAfterOperationDelete(
            account = account, operation = operation
        )

        val updatedAccount = account.copy(balance = updatedBalance)

        return operationRepository.deleteOperationAndUpdateAccount(
            operation = operation, updatedAccount = updatedAccount
        )
    }
}