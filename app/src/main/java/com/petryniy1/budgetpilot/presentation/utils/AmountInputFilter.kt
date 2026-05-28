package com.petryniy1.budgetpilot.presentation.utils

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

class AmountInputFilter : InputFilter {
    private val pattern: Pattern by lazy {
        Pattern.compile("[0-9]+((\\.[0-9]{0,2})?)|(\\.)?")
    }

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val destString = dest.toString()
        val sourceString = source.toString()
        val replacement = sourceString.subSequence(start, end).toString()
        val newVal = (destString.subSequence(0, dstart).toString() + replacement
                + destString.subSequence(dend, dest.length).toString())
        val matcher = pattern.matcher(newVal)
        return when {
            matcher.matches() -> null
            sourceString.isEmpty() -> destString.subSequence(dstart, dend)
            else -> ""
        }
    }
}