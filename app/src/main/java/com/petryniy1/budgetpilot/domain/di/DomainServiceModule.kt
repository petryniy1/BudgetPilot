package com.petryniy1.budgetpilot.domain.di

import com.petryniy1.budgetpilot.domain.repository.AccountRepository
import com.petryniy1.budgetpilot.domain.repository.BudgetOperationRepository
import com.petryniy1.budgetpilot.domain.service.AccountBalanceCalculator
import com.petryniy1.budgetpilot.domain.service.AccountBalancePolicy
import com.petryniy1.budgetpilot.domain.service.BasicAccountBalanceCalculator
import com.petryniy1.budgetpilot.domain.service.BasicBudgetOperationManager
import com.petryniy1.budgetpilot.domain.service.BudgetOperationManager
import com.petryniy1.budgetpilot.domain.service.DefaultAccountBalancePolicy
import com.petryniy1.budgetpilot.domain.validation.OperationValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class DomainServiceModule {
    @Provides
    @ViewModelScoped
    fun provideOperationValidator(): OperationValidator =
        OperationValidator()

    @Provides
    @ViewModelScoped
    fun provideAccountBalanceCalculator(): AccountBalanceCalculator =
        BasicAccountBalanceCalculator()

    @Provides
    @ViewModelScoped
    fun provideAccountBalancePolicy(): AccountBalancePolicy =
        DefaultAccountBalancePolicy()

    @Provides
    @ViewModelScoped
    fun provideBudgetOperationManager(
        accountRepository: AccountRepository,
        operationRepository: BudgetOperationRepository,
        operationValidator: OperationValidator,
        balanceCalculator: AccountBalanceCalculator,
        accountBalancePolicy: AccountBalancePolicy
    ): BudgetOperationManager =
        BasicBudgetOperationManager(
            accountRepository = accountRepository,
            operationRepository = operationRepository,
            operationValidator = operationValidator,
            balanceCalculator = balanceCalculator,
            accountBalancePolicy = accountBalancePolicy
        )
}