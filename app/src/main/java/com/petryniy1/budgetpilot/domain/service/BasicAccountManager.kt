package com.petryniy1.budgetpilot.domain.service

import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.results.AccountActionResult
import com.petryniy1.budgetpilot.domain.models.results.AccountValidationResult
import com.petryniy1.budgetpilot.domain.repository.AccountRepository
import com.petryniy1.budgetpilot.domain.validation.AccountValidator

class BasicAccountManager(
    private val accountRepository: AccountRepository,
    private val accountValidator: AccountValidator
) : AccountManager {
    override suspend fun createAccount(account: Account): AccountActionResult {
        return executeValidatedAccountAction(account) {
            accountRepository.createAccount(account)
        }
    }

    override suspend fun updateAccount(account: Account): AccountActionResult {
        return executeValidatedAccountAction(account) {
            accountRepository.updateAccount(account)
        }
    }

    override suspend fun deleteAccount(id: Int): AccountActionResult {
        return accountRepository.deleteAccount(id)
    }

    private suspend fun executeValidatedAccountAction(
        account: Account,
        action: suspend () -> AccountActionResult
    ): AccountActionResult {
        val validationResult = accountValidator.validate(account)

        if (validationResult != AccountValidationResult.Valid) {
            return AccountActionResult.ValidationError(validationResult)
        }

        return action()
    }
}