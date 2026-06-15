package com.petryniy1.budgetpilot.presentation.feature.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextPrimary
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextSecondary
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextShadow

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit
) {
    var currentSlideIndex by remember {
        mutableIntStateOf(0)
    }

    val isLastSlide = currentSlideIndex == OnboardingSlides.lastIndex

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050B1C))
    ) {
        OnboardingBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 36.dp),
            verticalArrangement = Arrangement.Top
        ) {
            OnboardingProgress(
                currentSlideIndex = currentSlideIndex,
                slidesCount = OnboardingSlides.size
            )

            Spacer(modifier = Modifier.height(86.dp))

            AnimatedContent(
                targetState = currentSlideIndex,
                transitionSpec = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 450)
                    ) togetherWith slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 450)
                    ) using SizeTransform(clip = false)
                },
                label = "Onboarding slide transition"
            ) { slideIndex ->
                OnboardingTextContent(
                    slide = OnboardingSlides[slideIndex]
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (isLastSlide) {
                        onFinish()
                    } else {
                        currentSlideIndex += 1
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF071129)
                )
            ) {
                Text(
                    text = if (isLastSlide) {
                        "Start using BudgetPilot"
                    } else {
                        "Next"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun OnboardingTextContent(
    slide: OnboardingSlide
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = slide.title,
            color = BudgetPilotTextPrimary,
            fontSize = 46.sp,
            lineHeight = 48.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                shadow = BudgetPilotTextShadow
            )
        )

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = slide.subtitle,
            color = BudgetPilotTextPrimary,
            fontSize = 20.sp,
            lineHeight = 26.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                shadow = BudgetPilotTextShadow
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = slide.description,
            color = BudgetPilotTextSecondary,
            fontSize = 18.sp,
            lineHeight = 26.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun OnboardingBackground() {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val width = size.width
        val height = size.height

        val firstPath = Path().apply {
            moveTo(width * 0.66f, 0f)
            cubicTo(
                width * 1.05f,
                height * 0.12f,
                width * 0.78f,
                height * 0.52f,
                width * 0.38f,
                height
            )
            lineTo(width, height)
            lineTo(width, 0f)
            close()
        }

        drawPath(
            path = firstPath,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF22D7FF),
                    Color(0xFF0054FF),
                    Color(0xFF1C0B4F)
                ),
                start = Offset(width * 0.42f, height * 0.08f),
                end = Offset(width, height)
            )
        )

        val secondPath = Path().apply {
            moveTo(0f, height * 0.73f)
            cubicTo(
                width * 0.28f,
                height * 0.58f,
                width * 0.52f,
                height * 0.86f,
                width * 0.78f,
                height
            )
            lineTo(0f, height)
            close()
        }

        drawPath(
            path = secondPath,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF24E4FF),
                    Color(0xFF004CFF),
                    Color(0xFF111B52)
                ),
                start = Offset(0f, height * 0.68f),
                end = Offset(width * 0.82f, height)
            )
        )
    }
}

@Composable
private fun OnboardingProgress(
    currentSlideIndex: Int,
    slidesCount: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(slidesCount) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(3.dp)
                    .background(
                        color = if (index <= currentSlideIndex) {
                            Color.White
                        } else {
                            Color.White.copy(alpha = 0.25f)
                        },
                        shape = CircleShape
                    )
            )
        }
    }
}

private data class OnboardingSlide(
    val title: String,
    val subtitle: String,
    val description: String
)

private val OnboardingSlides = listOf(
    OnboardingSlide(
        title = "Meet your\nBudgetPilot",
        subtitle = "A clear cockpit for your family budget.",
        description = "Track accounts, cash, cards and daily operations in one place without losing control of currencies and balances."
    ),
    OnboardingSlide(
        title = "Control your\ncash flow",
        subtitle = "Every operation changes the right account.",
        description = "Add income, expenses and transfers. BudgetPilot keeps account balances consistent while you focus on real-life spending."
    ),
    OnboardingSlide(
        title = "Understand\nwhat matters",
        subtitle = "Built for analytics, categories and smarter decisions.",
        description = "The app is prepared for spending categories, receipt scanning, bank sync and budget recommendations in future versions."
    )
)
