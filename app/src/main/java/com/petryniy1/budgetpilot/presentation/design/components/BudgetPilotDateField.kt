package com.petryniy1.budgetpilot.presentation.design.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotAccentBlue
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextPrimary
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextSecondary

@Composable
fun BudgetPilotDateField(
    value: String,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = modifier,
            label = {
                Text(text = label)
            },
            readOnly = true,
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = BudgetPilotTextPrimary,
                disabledLabelColor = BudgetPilotTextSecondary,
                disabledBorderColor = BudgetPilotTextSecondary,
                focusedBorderColor = BudgetPilotAccentBlue,
                unfocusedBorderColor = BudgetPilotTextSecondary
            )
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                )
        )
    }
}