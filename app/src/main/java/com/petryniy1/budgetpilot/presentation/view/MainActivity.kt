package com.petryniy1.budgetpilot.presentation.view

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import com.petryniy1.budgetpilot.R
import com.petryniy1.budgetpilot.databinding.ActivityMainBinding
import com.petryniy1.budgetpilot.presentation.viewModels.ActivityMainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by viewBinding()
    private val viewModel: ActivityMainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBottomMenu()

//        initBalance()

        initFilterButton()

    }

    private fun initFilterButton() {
        binding.imageViewFilter.setOnClickListener {
            findNavController(R.id.fragment_container).navigate(R.id.operationsFragment_to_bottomSheetFilterFragment)
        }
    }

    private fun initBottomMenu() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_nav_menu)
            .setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, navDestenetion, _ ->
            binding.apply {
                when (navDestenetion.id) {
                    R.id.operationsFragment -> imageViewFilter.visibility = VISIBLE
                    else -> findViewById<ImageView>(R.id.imageViewFilter).visibility = GONE
                }
            }
        }
    }

//    private fun initBalance() {
//
//        viewModel.viewModelScope.launch {
//            viewModel.balance.collect {
//                if (it != null) {
//                    balance = it
//                }
//
//            }
//        }
//        Log.d("balance", "initBalance $balance")
//
//
//        binding.textViewBalanceValue.text = getString(
//            R.string.msg_currency_byn_amount_format, balance / 100f
//        )
//    }

    companion object {
        private var balance: Long = 0
    }

}