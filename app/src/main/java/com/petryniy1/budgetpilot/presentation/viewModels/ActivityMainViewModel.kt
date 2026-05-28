package com.petryniy1.budgetpilot.presentation.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.petryniy1.budgetpilot.domain.repository.MoneyHoldersRepository
import com.petryniy1.budgetpilot.domain.repository.OperationsRepository
import javax.inject.Inject

@HiltViewModel
class ActivityMainViewModel @Inject constructor(

    private val moneyHoldersRepository: MoneyHoldersRepository,
    private val operationsRepository: OperationsRepository

) : ViewModel() {

    var moneyHoldersSumBalance: Long = 0
    var operationsSumValue: Long = 0

//    viewModel.viewModelScope.launch {
//        viewModel.moneyHoldersSumBalance.collect {
//            if (it != null) {
//                MoneyHolderFragment.moneyHoldersSumBalance = it
//            }
//            Log.d("myDebug", "moneyHoldersSumBalance ${MoneyHolderFragment.moneyHoldersSumBalance}")
//        }
//    }
//

//    val operationsSumValue = operationsRepository.getOperationsSumValue()
//    lifecycleScope.launchWhenResumed {
//        viewModel.operationsSumValue.collect {
//            if (it != null) {
//                OperationsFragment.operationsSumValue = it
//            }
//            Log.d("myDebug", "operationsSumValue ${OperationsFragment.operationsSumValue}")
//        }
//    }
//    private val _balance = MutableStateFlow<Long?>(null)
//    val balance: StateFlow<Long?> get() = _balance
//
//    init {
//        getBalance()
//    }
//
//    private fun getBalance() {
//        viewModelScope.launch {
//            _balance.value = moneyHoldersSumBalance operationsSumValue
//
//            Log.d("ActivityMainViewModelbalance", "initBalance ${_balance.value}")
//
//        }
//    }
}