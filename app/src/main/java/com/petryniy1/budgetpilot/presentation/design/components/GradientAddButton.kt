package com.petryniy1.budgetpilot.presentation.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotAccentButtonGradient
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextPrimary
import com.petryniy1.budgetpilot.presentation.design.budgetPilotOutline
import androidx.compose.ui.text.font.FontWeight

@Composable
fun GradientAddButton(
    text: String = "Add",
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                brush = BudgetPilotAccentButtonGradient,
                shape = RoundedCornerShape(18.dp)
            )
            .budgetPilotOutline()
            .clickable(onClick = onClick)
            .padding(horizontal = 26.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = BudgetPilotTextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}