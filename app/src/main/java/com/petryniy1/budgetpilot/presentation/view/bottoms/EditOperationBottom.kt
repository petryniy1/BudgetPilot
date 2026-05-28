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
import com.petryniy1.budgetpilot.databinding.BottomEditOperationBinding
import com.petryniy1.budgetpilot.presentation.view.fragments.OperationsFragment
import com.petryniy1.budgetpilot.presentation.viewModels.OperationsFragmentViewModel

@AndroidEntryPoint
class EditOperationBottom : BottomSheetDialogFragment() {

    private lateinit var binding: BottomEditOperationBinding
    private val viewModel: OperationsFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomEditOperationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val operationId: Int = requireArguments()
            .getInt(OperationsFragment.ID_FROM_OPERATIONS_FRAGMENT)

        initFields(operationId)

        initButtons(operationId)
    }

    private fun initButtons(operationId: Int?) = with(binding) {

        imageViewEdit.setOnClickListener {
            findNavController().navigate(
                R.id.editOperationBottom_to_addOperationBottom,
                bundleOf(OPERATION_ID_FROM_EDIT to operationId)
            )
        }

        imageViewDelete.setOnClickListener {
            if (operationId != null) {
                viewModel.deleteOperation(operationId)
                findNavController().navigate(R.id.editOperationBottom_to_operationsFragment)
            }
        }
    }

    private fun initFields(operationId: Int?) {
        if (operationId != null && operationId != 0) {

            viewModel.getOperationById(operationId)

            lifecycleScope.launchWhenResumed {
                viewModel.operation.collect {
                    if (it != null) {

                        val itemOperation = it.operationEntity
                        val itemMoneyHolder = it.moneyHolderEntity

                        binding.run {
                            imageViewCategory.setImageResource(itemOperation.categoryDrawable)
                            textViewValue.text = root.context.getString(
                                R.string.msg_currency_byn_amount_format,
                                itemOperation.value / 100f)
                            textViewCategory.text = itemOperation.category
                            textViewDate.text = itemOperation.date
                            textViewMoneyHolder.text = itemMoneyHolder.name

                            itemMoneyHolder.type?.let { it1 ->
                                imageViewMoneyHolder.setImageResource(
                                    it1
                                )
                            }
                            textViewComment.text = itemOperation.comment
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "EditBottom"
        const val OPERATION_ID_FROM_EDIT = "EDIT"
    }
}
