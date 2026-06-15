package com.petryniy1.budgetpilot.presentation.feature.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotMetaTextStyle
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotScreenGradient
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextPrimary
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextShadow

@Composable
fun AnalyticsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BudgetPilotScreenGradient)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Analytics",
            color = BudgetPilotTextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                shadow = BudgetPilotTextShadow
            )
        )

        Text(
            text = "Budget insights will appear here",
            style = BudgetPilotMetaTextStyle
        )

        Text(
            text = "Charts, categories and recommendations are planned for the next iteration.",
            color = BudgetPilotTextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            fontStyle = FontStyle.Italic,
            style = TextStyle(
                shadow = BudgetPilotTextShadow
            )
        )
    }
}
