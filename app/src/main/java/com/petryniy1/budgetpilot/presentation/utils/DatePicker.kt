package com.petryniy1.budgetpilot.presentation.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class DatePicker @Inject constructor() {

    private var currentSelectedDate: Long? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun showDatePicker(fragmentManager: FragmentManager, fragmentTag: String): String {
        val selectedDateInMillis = currentSelectedDate ?: System.currentTimeMillis()
        lateinit var dateAsFormattedText: String
        val dataPicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(selectedDateInMillis)
                .build()
                .apply {
                    addOnPositiveButtonClickListener { dateInMillis ->
                        val dateTime: LocalDateTime = LocalDateTime.ofInstant(
                            dateInMillis?.let { Instant.ofEpochMilli(it) }, ZoneId.systemDefault()
                        )

                        dateAsFormattedText =
                            dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    }
                }

        dataPicker.show(fragmentManager, fragmentTag)

        return "dateToString"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onDateSelected(date: Long?) {
        currentSelectedDate = date

        val dateTime: LocalDateTime = LocalDateTime.ofInstant(
            currentSelectedDate?.let { Instant.ofEpochMilli(it) }, ZoneId.systemDefault()
        )
        val dateAsFormattedText: String? =
            dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }
}
