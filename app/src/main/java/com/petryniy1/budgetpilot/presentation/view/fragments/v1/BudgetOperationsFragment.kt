package com.petryniy1.budgetpilot.presentation.view.fragments.v1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.petryniy1.budgetpilot.presentation.view.v1.BudgetOperationsRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BudgetOperationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                BudgetOperationsRoute()
            }
        }
    }
}