package com.petryniy1.budgetpilot.presentation.view.bottoms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import com.petryniy1.budgetpilot.R
import com.petryniy1.budgetpilot.databinding.BottomSheetFilterBinding
import com.petryniy1.budgetpilot.domain.models.Filter
import com.petryniy1.budgetpilot.presentation.recyclers.moneyholder.MoneyHolderArrayAdapter
import com.petryniy1.budgetpilot.presentation.viewModels.FilterSharedViewModel
import com.petryniy1.budgetpilot.presentation.viewModels.MoneyHolderFragmentViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class BottomSheetFilterFragment : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetFilterBinding
    private val moneyHoldersFragmentViewModel: MoneyHolderFragmentViewModel by viewModels()
    private val sharedViewModel: FilterSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetFilterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRadioGroup()

        initActvAddOperationAdapter()
    }

    private fun initRadioGroup() = with(binding) {
        radioGroup.setOnCheckedChangeListener { _, radioButton ->
            when (radioButton) {
                radioDate.id -> {
                    conditionEditText(filterDate)

                    filterDate.setOnClickListener {
                        showDatePicker()
                    }

                    filterDate.doAfterTextChanged { date ->
                        sharedViewModel.setFilter(
                            Filter.DateFilter(date.toString())
                        )
                    }
                }

                radioCategory.id -> {
                    conditionEditText(filterCategory)

                    filterCategory.doAfterTextChanged { category ->
                        sharedViewModel.setFilter(Filter.CategoryFilter(category.toString()))
                    }
                }

                radioMoneyHolder.id -> {

                    tilFilterMoneyHolder.visibility = VISIBLE

                    actvFilterMoneyHolder.setOnDismissListener {
                        moneyHolderId?.let { it1 -> Filter.MoneyHolderFilter(it1) }?.let { it2 ->
                            sharedViewModel.setFilter(
                                it2
                            )
                        }
                        dismiss()
                    }
                }

                radioClean.id -> {
                    sharedViewModel.setFilter(Filter.EmptyFilter)

                    dismiss()
                }
            }
        }
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
        binding.filterDate.setText(dateAsFormattedText)
    }

    private fun conditionEditText(editText: EditText) {
        setEditTextVisibility(editText)
        dismissBottom(editText)
    }

    private fun setEditTextVisibility(editText: EditText?) = with(binding) {
        when (editText) {

            filterDate -> {
                filterDate.visibility = VISIBLE
                filterCategory.visibility = GONE
                tilFilterMoneyHolder.visibility = GONE
            }

            filterCategory -> {
                filterCategory.visibility = VISIBLE
                filterDate.visibility = GONE
                tilFilterMoneyHolder.visibility = GONE
            }

            tilFilterMoneyHolder -> {
               tilFilterMoneyHolder.visibility = VISIBLE
                filterDate.visibility = GONE
                filterCategory.visibility = GONE
            }

        }
    }

    private val onEditorActionListener: TextView.OnEditorActionListener =
        TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == IME_ACTION_DONE) {

                dismiss()
                return@OnEditorActionListener true

            }
            false
        }

    private fun dismissBottom(editText: EditText) {
        editText.setOnEditorActionListener(onEditorActionListener)
    }

    private fun initActvAddOperationAdapter() {
        lifecycleScope.launchWhenResumed {
            moneyHoldersFragmentViewModel.getAllMoneyHoldersListFlow().collect { list ->
                val adapter = MoneyHolderArrayAdapter(requireContext(), list)
                binding.run {
                    actvFilterMoneyHolder.setAdapter(adapter)
                    actvFilterMoneyHolder.setOnItemClickListener { _, _, position, _ ->
                        moneyHolderId = adapter.getItem(position)?.id
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "BottomRadioGroupFragment"
        private var currentSelectedDate: Long? = null
        private var moneyHolderId: Int? = null
    }
}

