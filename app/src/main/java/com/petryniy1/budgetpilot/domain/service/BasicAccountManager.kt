package com.petryniy1.budgetpilot.domain.service

import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.repository.AccountRepository
import com.petryniy1.budgetpilot.domain.results.AccountActionResult
import com.petryniy1.budgetpilot.domain.results.AccountValidationResult
import com.petryniy1.budgetpilot.domain.validation.AccountValidator

class BasicAccountManager(
    private val accountRepository: AccountRepository,
    private val accountValidator: AccountValidator
) : AccountManager {
    override suspend fun createAccount(account: Account): AccountActionResult {
        return executeValidatedAccountAction(
            account = account,
            excludeCurrentAccount = false
        ) {
            accountRepository.createAccount(account)
        }
    }

    override suspend fun updateAccount(account: Account): AccountActionResult {
        return executeValidatedAccountAction(
            account = account,
            excludeCurrentAccount = true
        ) {
            accountRepository.updateAccount(account)
        }
    }

    override suspend fun deleteAccount(id: Int): AccountActionResult {
        return accountRepository.deleteAccount(id)
    }

    private suspend fun executeValidatedAccountAction(
        account: Account,
        excludeCurrentAccount: Boolean,
        action: suspend () -> AccountActionResult
    ): AccountActionResult {
        val validationResult = accountValidator.validate(account)

        if (validationResult != AccountValidationResult.Valid) {
            return AccountActionResult.ValidationError(validationResult)
        }

        val duplicateNameExists = accountRepository.existsAccountWithName(
            name = account.name,
            excludeId = if (excludeCurrentAccount) account.id else null
        )

        if (duplicateNameExists) {
            return AccountActionResult.DuplicateAccountName
        }

        return action()
    }
}