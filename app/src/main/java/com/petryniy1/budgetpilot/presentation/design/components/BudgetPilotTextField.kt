package com.petryniy1.budgetpilot.presentation.design.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotAccentBlue
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotError
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextPrimary
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextSecondary

@Composable
fun BudgetPilotTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = {
            Text(text = label)
        },
        supportingText = error?.let { errorText ->
            {
                Text(text = errorText)
            }
        },
        isError = error != null,
        singleLine = singleLine,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = BudgetPilotTextPrimary,
            unfocusedTextColor = BudgetPilotTextPrimary,
            disabledTextColor = BudgetPilotTextSecondary,
            focusedLabelColor = BudgetPilotAccentBlue,
            unfocusedLabelColor = BudgetPilotTextSecondary,
            disabledLabelColor = BudgetPilotTextSecondary,
            cursorColor = BudgetPilotAccentBlue,
            focusedBorderColor = BudgetPilotAccentBlue,
            unfocusedBorderColor = BudgetPilotTextSecondary,
            disabledBorderColor = BudgetPilotTextSecondary,
            errorBorderColor = BudgetPilotError,
            errorLabelColor = BudgetPilotError,
            errorSupportingTextColor = BudgetPilotError
        )
    )
}

