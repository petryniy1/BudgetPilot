package com.petryniy1.budgetpilot.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import com.petryniy1.budgetpilot.data.RepositoryImpl
import com.petryniy1.budgetpilot.data.repository.AccountRepositoryImpl
import com.petryniy1.budgetpilot.data.repository.BudgetOperationRepositoryImpl
import com.petryniy1.budgetpilot.domain.repository.AccountRepository
import com.petryniy1.budgetpilot.domain.repository.BudgetOperationRepository
import com.petryniy1.budgetpilot.domain.repository.MoneyHoldersRepository
import com.petryniy1.budgetpilot.domain.repository.OperationsRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainModule {

    @Binds
    @ViewModelScoped
    abstract fun provideMoneyHoldersRepository(repository: RepositoryImpl): MoneyHoldersRepository

    @Binds
    @ViewModelScoped
    abstract fun provideOperationRepository(repository: RepositoryImpl): OperationsRepository

    @Binds
    @ViewModelScoped
    abstract fun bindAccountRepository(
        repository: AccountRepositoryImpl
    ): AccountRepository

    @Binds
    @ViewModelScoped
    abstract fun bindBudgetOperationRepository(
        repository: BudgetOperationRepositoryImpl
    ): BudgetOperationRepository

}
