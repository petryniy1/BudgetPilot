package com.petryniy1.budgetpilot.presentation.view.bottoms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import com.petryniy1.budgetpilot.R
import com.petryniy1.budgetpilot.databinding.BottomAddMoneyHolderBinding
import com.petryniy1.budgetpilot.domain.models.MoneyHolder
import com.petryniy1.budgetpilot.presentation.utils.AmountInputFilter
import com.petryniy1.budgetpilot.presentation.viewModels.MoneyHolderFragmentViewModel

@AndroidEntryPoint
class AddMoneyHolderBottom : BottomSheetDialogFragment() {

    private lateinit var binding: BottomAddMoneyHolderBinding
    private val viewModel: MoneyHolderFragmentViewModel by viewModels()
    private var type: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomAddMoneyHolderBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val moneyHolderId: Int = requireArguments()
            .getInt(EditMoneyHolderBottom.MONEY_HOLDER_ID_FROM_EDIT)

        initTypeAdapter()

        initTextFields()

        initSaveButton(moneyHolderId)

        putFieldsFromMoneyHolder(moneyHolderId)
    }

    private fun putFieldsFromMoneyHolder(moneyHolderId: Int?) {

        if (moneyHolderId != null && moneyHolderId != 0) {

            viewModel.getMoneyHolderById(moneyHolderId)
            lifecycleScope.launchWhenResumed {
                viewModel.moneyHolder.collect { moneyHolder ->
                    binding.run {
                        tilName.editText?.setText(moneyHolder?.name)
                        tilBalance.editText?.setText((moneyHolder?.balance?.div(100f))
                            .toString())
                    }
                }
            }
        }
    }

    private fun initSaveButton(id: Int?) = with(binding) {

        buttonSave.setOnClickListener {
            when {
                tilName.editText?.text.isNullOrEmpty() -> tilName.error =
                    getString(R.string.errorMonyHolderText)
                tilType.editText?.text.isNullOrEmpty() -> tilType.error =
                    getString(R.string.errorMonyHolderType)
                tilBalance.editText?.text.isNullOrEmpty() -> tilBalance.error =
                    getString(R.string.errorMonyHolderBalance)
                else -> {
                    if (id != null && id != 0) updateMoneyHolder(id) else addMoneyHolder()
                    dismiss()
                    findNavController().navigate(R.id.addMoneyHolderBottom_to_moneyHolderFragment)
                }
            }
        }
    }

    private fun updateMoneyHolder(id: Int) = with(binding) {
        viewModel.updateMoneyHolder(
            MoneyHolder(
                id = id,
                name = tilName.editText?.text.toString(),
                type = type,
                balance = tilBalance.editText?.text.toString().toFloat().let { (it * 100).toLong() }
            )
        )
    }

    private fun addMoneyHolder() = with(binding) {
        viewModel.addMoneyHolder(
            MoneyHolder(
                name = tilName.editText?.text.toString(),
                type = type,
                balance = tilBalance.editText?.text.toString().toFloat().let { (it * 100).toLong() }
            )
        )
    }

    private fun initTextFields() = with(binding) {
        tilName.editText?.doAfterTextChanged { tilName.error = null }
        tilType.editText?.doAfterTextChanged { tilType.error = null }
        tilBalance.editText?.doAfterTextChanged { tilBalance.error = null }
        tilBalance.editText?.filters = arrayOf(AmountInputFilter())
    }

    private fun initTypeAdapter() = with(binding) {
        actvType.setAdapter(operationStatusAdapter)
        actvType.setOnItemClickListener { _, _, position, _ ->
            type = when (operationStatusAdapter.getItem(position)) {
                getString(R.string.typeCash) -> R.drawable.ic_cash
                getString(R.string.typeNonCash) -> R.drawable.ic_credit_card
                else -> 0
            }
        }
    }

    private val operationStatusAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter(
            requireContext(),
            R.layout.item_type_menu_money_holder,
            listOf(
                getString(R.string.typeCash),
                getString(R.string.typeNonCash)
            )
        )
    }
}
