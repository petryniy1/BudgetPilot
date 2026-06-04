package com.petryniy1.budgetpilot.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.petryniy1.budgetpilot.domain.models.Filter
import com.petryniy1.budgetpilot.domain.models.Operation
import com.petryniy1.budgetpilot.domain.models.OperationWithMoneyHolder
import com.petryniy1.budgetpilot.domain.repository.OperationsRepository
import javax.inject.Inject

@HiltViewModel
class OperationsFragmentViewModel @Inject constructor(

    private val operationsRepository: OperationsRepository,

    ) : ViewModel() {

    private val _operation = MutableStateFlow<OperationWithMoneyHolder?>(null)
    val operation: StateFlow<OperationWithMoneyHolder?> get() = _operation.asStateFlow()

    fun getFilteredOperationsListFlow(filter: Filter): Flow<List<OperationWithMoneyHolder?>> {

        return when (filter) {
            is Filter.EmptyFilter -> getAllOperationsListFlow()

            is Filter.DateFilter -> getAllOperationsListFlow().map {
                it.filter { operationWithMoneyHolder ->
                    operationWithMoneyHolder?.operationEntity?.date?.contains(filter.date) ?: false
                }
            }

            is Filter.CategoryFilter -> getAllOperationsListFlow().map {
                it.filter { operationWithMoneyHolder ->
                    operationWithMoneyHolder?.operationEntity?.category
                        ?.lowercase()
                        ?.contains(filter.category.lowercase()) ?: false
                }
            }

            is Filter.MoneyHolderFilter -> getAllOperationsListFlow().map {
                it.filter { operationWithMoneyHolder ->
                    operationWithMoneyHolder?.operationEntity?.accountId == filter.id
                }
            }
        }
    }

    private fun getAllOperationsListFlow() = operationsRepository.getAllOperations()

    fun getOperationById(id: Int) {
        viewModelScope.launch {
            operationsRepository.getOperationById(id).collect {
                _operation.value = it
            }
        }
    }

    fun addOperation(operation: Operation) {
        viewModelScope.launch {
            operationsRepository.addOperation(operation)
        }
    }

    fun updateOperation(operation: Operation) {
        viewModelScope.launch {
            operationsRepository.updateOperation(operation)
        }
    }

    fun deleteOperation(id: Int) {
        viewModelScope.launch {
            operationsRepository.deleteOperation(id)
        }
    }
}