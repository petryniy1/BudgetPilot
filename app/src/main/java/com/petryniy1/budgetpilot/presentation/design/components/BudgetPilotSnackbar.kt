package com.petryniy1.budgetpilot.presentation.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotPrimaryCardGradient
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextPrimary
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextShadow
import com.petryniy1.budgetpilot.presentation.design.budgetPilotOutline

@Composable
fun BudgetPilotSnackbar(
    snackbarData: SnackbarData
) {
    Box(
        modifier = Modifier
            .background(
                brush = BudgetPilotPrimaryCardGradient,
                shape = RoundedCornerShape(18.dp)
            )
            .budgetPilotOutline(
                shape = RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Text(
            text = snackbarData.visuals.message,
            color = BudgetPilotTextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            style = TextStyle(
                shadow = BudgetPilotTextShadow
            )
        )
    }
}

