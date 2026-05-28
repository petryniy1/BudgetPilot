package com.petryniy1.budgetpilot.presentation.view.bottoms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import com.petryniy1.budgetpilot.R
import com.petryniy1.budgetpilot.databinding.BottomEditMoneyHolderBinding
import com.petryniy1.budgetpilot.presentation.view.fragments.MoneyHolderFragment
import com.petryniy1.budgetpilot.presentation.viewModels.MoneyHolderFragmentViewModel

@AndroidEntryPoint
class EditMoneyHolderBottom : BottomSheetDialogFragment() {

    private lateinit var binding: BottomEditMoneyHolderBinding
    private val viewModel: MoneyHolderFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomEditMoneyHolderBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val moneyHolderId: Int = requireArguments().getInt(MoneyHolderFragment.MONEY_HOLDER_ID_FROM_HOLDER)

        initButtons(moneyHolderId)

        initFields(moneyHolderId)
    }

    private fun initFields(moneyHolderId: Int?) {
        if (moneyHolderId != null && moneyHolderId != 0) {
            viewModel.getMoneyHolderById(moneyHolderId)

            lifecycleScope.launchWhenResumed {
                viewModel.moneyHolder.collect { moneyHolder ->
                    binding.run {
                        moneyHolder?.type?.let { imageViewEdType.setImageResource(it) }
                        textEdViewName.text = moneyHolder?.name
                        textViewEdBalance.text = getString(R.string.msg_currency_byn_amount_format,
                            moneyHolder?.balance?.div(100f) ?: 100f)

                    }
                }
            }
        }
    }

    private fun initButtons(moneyHolderId: Int?) = with(binding) {

        imageViewEdEdit.setOnClickListener {
            findNavController().navigate(
                R.id.editMoneyHolderBottom_to_addMoneyHolderBottom,
                bundleOf(MONEY_HOLDER_ID_FROM_EDIT to moneyHolderId)
            )
        }

        imageViewEdDelete.setOnClickListener {
            if (moneyHolderId != null) {
                viewModel.deleteMoneyHolder(id = moneyHolderId)
                findNavController().navigate(R.id.editMoneyHolderBottom_to_moneyHolderFragment)
            }
        }
    }

    companion object {
        const val MONEY_HOLDER_ID_FROM_EDIT = "EDIT"
    }
}

