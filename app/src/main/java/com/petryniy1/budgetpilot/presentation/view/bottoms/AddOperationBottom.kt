package com.petryniy1.budgetpilot.presentation.view.bottoms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import com.petryniy1.budgetpilot.R
import com.petryniy1.budgetpilot.databinding.BottomAddOperationBinding
import com.petryniy1.budgetpilot.domain.models.Operation
import com.petryniy1.budgetpilot.presentation.recyclers.moneyholder.MoneyHolderArrayAdapter
import com.petryniy1.budgetpilot.presentation.utils.AmountInputFilter
import com.petryniy1.budgetpilot.presentation.viewModels.MoneyHolderFragmentViewModel
import com.petryniy1.budgetpilot.presentation.viewModels.OperationsFragmentViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AddOperationBottom : BottomSheetDialogFragment() {

    private lateinit var binding: BottomAddOperationBinding
    private val operationsViewModel: OperationsFragmentViewModel by viewModels()
    private val moneyHoldersFragmentViewModel: MoneyHolderFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomAddOperationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val operationId: Int = requireArguments().getInt(EditOperationBottom.OPERATION_ID_FROM_EDIT)

        initActvAddOperationAdapter()

        initTextFields()

        initCategory()

        initTypeItem()

        initSaveButton(operationId)

        putFieldsFromOperation(operationId)

    }

    private fun putFieldsFromOperation(operationId: Int?) {

        if (operationId != null && operationId != 0) {

            operationsViewModel.getOperationById(operationId)

            lifecycleScope.launchWhenResumed {
                operationsViewModel.operation.collect {
                    if (it != null) {

                        val item = it.operationEntity

                        binding.run {

                            if (item.value > 0) {
                                chipGroupDC.check(chipDebit.id)
                            } else {
                                chipGroupDC.check(chipCredit.id)
                            }

                            tilAddValue.editText?.setText(
                                root.context.getString(
                                        R.string.msg_currency_byn_amount_format,
                                        item.value.div(100f)
                                    )
                            )

                            chipGroupType.check(
                                when (item.category) {
                                    getString(R.string.categoryCar) -> chipCar.id

                                    getString(R.string.categoryProd) -> chipProducts.id

                                    getString(R.string.categoryPets) -> chipPets.id

                                    getString(R.string.categoryChild) -> chipChildren.id

                                    getString(R.string.categoryHouse) -> chipHouse.id

                                    getString(R.string.categoryRelax) -> chipRelax.id

                                    else -> {
                                        0
                                    }
                                }
                            )

                            tilAddDate.editText?.setText(item.date)

                            tilAddComments.editText?.setText(item.comment)
                        }
                    }
                }
            }
        }
    }

    private fun initSaveButton(operationId: Int?) = with(binding) {

        buttonOperationSave.setOnClickListener {
            when {
                !initTypeItem() -> Toast.makeText(
                    context, getString(R.string.errorAddOperationType), Toast.LENGTH_LONG
                ).show()

                tilAddValue.editText?.text.isNullOrEmpty() ->
                    tilAddValue.error = getString(R.string.errorAddOperationValue)

                !initCategory() -> Toast.makeText(
                    context,
                    getString(R.string.errorAddOperationCategory),
                    Toast.LENGTH_LONG
                ).show()

                tilAddMoneyHolder.editText?.text.isNullOrEmpty() ->
                    tilAddMoneyHolder.error = getString(R.string.errorAddOperationMoneyHolder)

                tilAddDate.editText?.text.isNullOrEmpty() ->
                    tilAddDate.error = getString(R.string.errorAddOperationDate)

                else -> {

                    if (operationId != null && operationId != 0) updateOperation(operationId)
                    else addOperation()

                    dismiss()

                    findNavController().navigate(R.id.addOperationBottom_to_operationsFragment)

                }
            }
        }
    }

    private fun updateOperation(operationId: Int?) = with(binding) {
        val operation = moneyHolderId?.let { it ->
            Operation(
                id = operationId ?: 0,
                category = category,
                moneyHolderId = it,
                value = tilAddValue.editText?.text.toString().toFloat()
                    .let { (it * value).toLong() },
                categoryDrawable = categoryDrawable,
                date = tilAddDate.editText?.text.toString(),
                comment = tilAddComments.editText?.text.toString()
            )
        }

        if (operation != null) {
            operationsViewModel.updateOperation(operation)
        }

        Toast.makeText(requireContext(), getString(R.string.toastAddOperation), Toast.LENGTH_SHORT)
            .show()
    }


    private fun addOperation() = with(binding) {
        val operation = moneyHolderId?.let { it1 ->
            Operation(
                category = category,
                moneyHolderId = it1,
                value = tilAddValue.editText?.text.toString().toFloat()
                    .let { (it * value).toLong() },
                categoryDrawable = categoryDrawable,
                date = tilAddDate.editText?.text.toString(),
                comment = tilAddComments.editText?.text.toString()
            )
        }

        Toast.makeText(requireContext(), getString(R.string.toastAddOperation), Toast.LENGTH_SHORT)
            .show()

        if (operation != null) {
            operationsViewModel.addOperation(operation)
        }
    }

    private fun initTextFields() = with(binding) {
        tilAddValue.editText?.doAfterTextChanged { tilAddValue.error = null }
        tilAddMoneyHolder.editText?.doAfterTextChanged { tilAddMoneyHolder.error = null }
        tilAddDate.editText?.doAfterTextChanged { tilAddDate.error = null }
        tilAddValue.editText?.filters = arrayOf(AmountInputFilter())
        binding.tilAddDateClick.setOnClickListener { showDatePicker() }
    }

    private fun initCategory(): Boolean = with(binding) {
        chipGroupType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                chipCar.id -> {
                    category = getString(R.string.categoryCar)
                    categoryDrawable = R.drawable.ic_car
                    initCategoryBoolean = true
                }

                chipProducts.id -> {
                    category = getString(R.string.categoryProd)
                    categoryDrawable = R.drawable.ic_products
                    initCategoryBoolean = true
                }

                chipPets.id -> {
                    category = getString(R.string.categoryPets)
                    categoryDrawable = R.drawable.ic_pets
                    initCategoryBoolean = true
                }

                chipChildren.id -> {
                    category = getString(R.string.categoryChild)
                    categoryDrawable = R.drawable.ic_child
                    initCategoryBoolean = true
                }

                chipHouse.id -> {
                    category = getString(R.string.categoryHouse)
                    categoryDrawable = R.drawable.ic_house
                    initCategoryBoolean = true
                }

                chipRelax.id -> {
                    category = getString(R.string.categoryRelax)
                    categoryDrawable = R.drawable.ic_coffee
                    initCategoryBoolean = true
                }
                else -> initCategoryBoolean = false
            }
        }
        return initCategoryBoolean
    }

    private fun initTypeItem(): Boolean = with(binding) {
        chipGroupDC.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {

                chipCredit.id -> {
                    value = 1 * 100 * -1
                    initTypeItemBoolean = true
                }

                chipDebit.id -> {
                    value = 1 * 100
                    initTypeItemBoolean = true
                }

                else -> initTypeItemBoolean = false
            }
        }
        return initTypeItemBoolean
    }

    private fun showDatePicker() {
        val selectedDateInMillis = currentSelectedDate ?: System.currentTimeMillis()

        val dataPicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.datePickerText))
                .setSelection(selectedDateInMillis)
                .build()
                .apply {
                    addOnPositiveButtonClickListener { dateInMillis ->
                        onDateSelected(
                            dateInMillis
                        )
                    }
                }

        dataPicker.show(childFragmentManager, TAG)
    }

    private fun onDateSelected(date: Long?) {
        currentSelectedDate = date

        val dateTime: LocalDateTime = LocalDateTime.ofInstant(
            currentSelectedDate?.let { Instant.ofEpochMilli(it) }, ZoneId.systemDefault()
        )
        val dateAsFormattedText: String? =
            dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        binding.tilAddDate.editText?.setText(dateAsFormattedText)
    }

    private fun initActvAddOperationAdapter() {
        lifecycleScope.launchWhenResumed {
            moneyHoldersFragmentViewModel.getAllMoneyHoldersListFlow().collect { list ->
                val adapter = MoneyHolderArrayAdapter(requireContext(), list)
                binding.run {
                    actvAddOperation.setAdapter(adapter)
                    actvAddOperation.setOnItemClickListener { _, _, position, _ ->
                        moneyHolderId = adapter.getItem(position)?.id
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "AddOperationBottom"
        private var category: String = "0"
        private var categoryDrawable: Int = 0
        private var moneyHolderId: Int? = null
        private var initCategoryBoolean = false
        private var initTypeItemBoolean = false
        private var currentSelectedDate: Long? = null
        private var value: Long = 0
    }
}


