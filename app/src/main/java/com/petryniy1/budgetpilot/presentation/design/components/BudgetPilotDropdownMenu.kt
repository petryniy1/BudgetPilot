package com.petryniy1.budgetpilot.presentation.design.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotAccentBlue
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotError
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextPrimary

@Composable
fun BudgetPilotDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        containerColor = Color(0xFF071129),
        tonalElevation = 0.dp,
        shadowElevation = 12.dp,
        content = content
    )
}

@Composable
fun BudgetPilotDropdownMenuItem(
    text: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    DropdownMenuItem(
        text = {
            Text(text = text)
        },
        onClick = onClick,
        colors = MenuDefaults.itemColors(
            textColor = if (isDestructive) {
                BudgetPilotError
            } else {
                BudgetPilotTextPrimary
            },
            leadingIconColor = BudgetPilotAccentBlue,
            trailingIconColor = BudgetPilotAccentBlue
        )
    )
}