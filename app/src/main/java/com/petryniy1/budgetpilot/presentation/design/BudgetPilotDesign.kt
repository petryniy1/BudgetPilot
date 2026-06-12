package com.petryniy1.budgetpilot.presentation.design

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.unit.dp

val BudgetPilotScreenGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF0B1220),
        Color(0xFF002967),
        Color(0xFF0B1220)
    )
)

val BudgetPilotAccentButtonGradient = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF050B14),
        Color(0xFF0B2342),
        Color(0xFF0F4C8A)
    )
)

val BudgetPilotPrimaryCardGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF050B14),
        Color(0xFF0F4C8A)
    )
)

val BudgetPilotCurrencyFrameOverlay = Color(0x14FFFFFF)
val BudgetPilotCurrencyFrameBorder = Color(0x30FFFFFF)

val BudgetPilotTextPrimary = Color(0xFFF5F7FF)
val BudgetPilotTextSecondary = Color(0xFFB9C3D9)
val BudgetPilotAmountNeutral = Color(0xFFD6DCE8)

val BudgetPilotSoftTextShadow = Shadow(
    color = Color(0x66000000),
    offset = Offset(0f, 2f),
    blurRadius = 8f
)

val BudgetPilotSurfaceOutlineColor = Color(0x66D6E4FF)
val BudgetPilotSurfaceOutlineShape = RoundedCornerShape(18.dp)

fun Modifier.budgetPilotOutline(
    shape: Shape = BudgetPilotSurfaceOutlineShape
): Modifier {
    return this.border(
        width = 1.dp,
        color = BudgetPilotSurfaceOutlineColor,
        shape = shape
    )
}