package com.petryniy1.budgetpilot.presentation.design.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotAccentBlue
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextSecondary

@Composable
fun BoxScope.BudgetPilotVerticalScrollIndicator(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    width: Dp = 4.dp,
    minimumHeight: Dp = 32.dp
) {
    if (scrollState.maxValue <= 0) return

    val density = LocalDensity.current
    val widthPx = with(density) {
        width.toPx()
    }
    val minimumHeightPx = with(density) {
        minimumHeight.toPx()
    }

    Canvas(
        modifier = modifier
            .matchParentSize()
            .padding(
                top = 4.dp,
                end = 2.dp,
                bottom = 4.dp
            )
    ) {
        val viewportHeight = size.height

        if (viewportHeight <= 0f) {
            return@Canvas
        }

        val totalContentHeight =
            viewportHeight + scrollState.maxValue

        val calculatedThumbHeight =
            viewportHeight * (viewportHeight / totalContentHeight)

        val thumbHeight = calculatedThumbHeight.coerceIn(
            minimumValue = minimumHeightPx.coerceAtMost(viewportHeight),
            maximumValue = viewportHeight
        )

        val scrollFraction =
            (
                    scrollState.value.toFloat() /
                            scrollState.maxValue.toFloat()
                    ).coerceIn(0f, 1f)

        val thumbTop =
            (viewportHeight - thumbHeight) * scrollFraction

        val indicatorLeft = size.width - widthPx
        val cornerRadius = CornerRadius(
            x = widthPx / 2f,
            y = widthPx / 2f
        )

        drawRoundRect(
            color = BudgetPilotTextSecondary.copy(alpha = 0.18f),
            topLeft = Offset(
                x = indicatorLeft,
                y = 0f
            ),
            size = Size(
                width = widthPx,
                height = viewportHeight
            ),
            cornerRadius = cornerRadius
        )

        drawRoundRect(
            color = BudgetPilotAccentBlue.copy(alpha = 0.9f),
            topLeft = Offset(
                x = indicatorLeft,
                y = thumbTop
            ),
            size = Size(
                width = widthPx,
                height = thumbHeight
            ),
            cornerRadius = cornerRadius
        )
    }
}
