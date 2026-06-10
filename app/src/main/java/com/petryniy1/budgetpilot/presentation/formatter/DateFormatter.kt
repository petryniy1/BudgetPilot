package com.petryniy1.budgetpilot.presentation.formatter

import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val OperationDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd.MM.yyyy")

fun LocalDate.formatForOperationDisplay(): String {
    return format(OperationDateFormatter)
}