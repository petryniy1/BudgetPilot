package com.petryniy1.budgetpilot.domain.models

sealed class Filter {
    data class DateFilter(val date: String) : Filter()
    data class CategoryFilter(val category: String) : Filter()
    data class MoneyHolderFilter(val id: Int) : Filter()
    object EmptyFilter : Filter()
}