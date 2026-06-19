package com.petryniy1.budgetpilot.presentation.feature.tutorial

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petryniy1.budgetpilot.R
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotAccentBlue
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotAmountNeutral
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotMetaTextStyle
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotPrimaryCardGradient
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotScreenGradient
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextPrimary
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextSecondary
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextShadow
import com.petryniy1.budgetpilot.presentation.design.budgetPilotOutline

@Composable
fun GuidedTutorialScreen(
    onFinish: () -> Unit
) {
    var currentStepIndex by remember {
        mutableIntStateOf(0)
    }

    val currentStep = TutorialSteps[currentStepIndex]
    val isLastStep = currentStepIndex == TutorialSteps.lastIndex

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BudgetPilotScreenGradient)
    ) {
        AnimatedContent(
            targetState = currentStep.scene,
            modifier = Modifier.fillMaxSize(),
            transitionSpec = {
                fadeIn(animationSpec = tween(durationMillis = 260)) togetherWith
                    fadeOut(animationSpec = tween(durationMillis = 180))
            },
            label = "Tutorial scene transition"
        ) { scene ->
            TutorialMockApp(scene = scene)
        }

        TutorialSpotlightOverlay(
            step = currentStep,
            isLastStep = isLastStep,
            onNext = {
                if (isLastStep) {
                    onFinish()
                } else {
                    currentStepIndex += 1
                }
            }
        )
    }
}

@Composable
fun GuidedTutorialFocusCalibrationScreen() {
    var currentStepIndex by remember {
        mutableIntStateOf(0)
    }
    val adjustments = remember {
        mutableStateMapOf<TutorialTarget, TutorialFocusAdjustment>().apply {
            putAll(TutorialFocusAdjustments)
        }
    }

    val currentStep = TutorialSteps[currentStepIndex]
    val currentAdjustment = adjustments[currentStep.target]
        ?: TutorialFocusAdjustment()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020714))
    ) {
        Box(
            modifier = Modifier
                .width(TutorialCalibrationPhoneWidth)
                .fillMaxHeight()
                .background(BudgetPilotScreenGradient)
        ) {
            TutorialMockApp(scene = currentStep.scene)

            if (currentStep.target != TutorialTarget.Finish) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            compositingStrategy = CompositingStrategy.Offscreen
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xB3020714))
                    )

                    TutorialFocusFrame(
                        target = currentStep.target,
                        adjustment = currentAdjustment
                    )
                }
            }
        }

        TutorialCalibrationControls(
            stepIndex = currentStepIndex,
            stepsCount = TutorialSteps.size,
            title = currentStep.title,
            target = currentStep.target,
            adjustment = currentAdjustment,
            onPreviousStep = {
                if (currentStepIndex > 0) {
                    currentStepIndex -= 1
                }
            },
            onNextStep = {
                if (currentStepIndex < TutorialSteps.lastIndex) {
                    currentStepIndex += 1
                }
            },
            onAdjustmentChange = { adjustment ->
                adjustments[currentStep.target] = adjustment
            }
        )
    }
}

@Composable
private fun TutorialCalibrationControls(
    stepIndex: Int,
    stepsCount: Int,
    title: String,
    target: TutorialTarget,
    adjustment: TutorialFocusAdjustment,
    onPreviousStep: () -> Unit,
    onNextStep: () -> Unit,
    onAdjustmentChange: (TutorialFocusAdjustment) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(190.dp)
            .background(Color(0xFF071129))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Focus calibration",
            color = BudgetPilotTextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Step ${stepIndex + 1}/$stepsCount\n$title\n${target.name}",
            color = BudgetPilotAccentBlue,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            TutorialCalibrationButton(
                text = "Prev",
                onClick = onPreviousStep
            )
            TutorialCalibrationButton(
                text = "Next",
                onClick = onNextStep
            )
        }

        Text(
            text = "Position",
            color = BudgetPilotTextPrimary,
            fontWeight = FontWeight.Bold
        )

        TutorialCalibrationButton(
            text = "Up 4dp",
            onClick = {
                onAdjustmentChange(
                    adjustment.copy(offsetY = adjustment.offsetY - 4.dp)
                )
            }
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            TutorialCalibrationButton(
                text = "Left",
                onClick = {
                    onAdjustmentChange(
                        adjustment.copy(offsetX = adjustment.offsetX - 4.dp)
                    )
                }
            )
            TutorialCalibrationButton(
                text = "Right",
                onClick = {
                    onAdjustmentChange(
                        adjustment.copy(offsetX = adjustment.offsetX + 4.dp)
                    )
                }
            )
        }

        TutorialCalibrationButton(
            text = "Down 4dp",
            onClick = {
                onAdjustmentChange(
                    adjustment.copy(offsetY = adjustment.offsetY + 4.dp)
                )
            }
        )

        Text(
            text = "Size",
            color = BudgetPilotTextPrimary,
            fontWeight = FontWeight.Bold
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            TutorialCalibrationButton(
                text = "W−",
                onClick = {
                    onAdjustmentChange(
                        adjustment.copy(
                            scaleX = (adjustment.scaleX - 0.05f)
                                .coerceAtLeast(0.5f)
                        )
                    )
                }
            )
            TutorialCalibrationButton(
                text = "W+",
                onClick = {
                    onAdjustmentChange(
                        adjustment.copy(scaleX = adjustment.scaleX + 0.05f)
                    )
                }
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            TutorialCalibrationButton(
                text = "H−",
                onClick = {
                    onAdjustmentChange(
                        adjustment.copy(
                            scaleY = (adjustment.scaleY - 0.05f)
                                .coerceAtLeast(0.5f)
                        )
                    )
                }
            )
            TutorialCalibrationButton(
                text = "H+",
                onClick = {
                    onAdjustmentChange(
                        adjustment.copy(scaleY = adjustment.scaleY + 0.05f)
                    )
                }
            )
        }

        TutorialCalibrationButton(
            text = "Reset",
            onClick = {
                onAdjustmentChange(
                    TutorialFocusAdjustments[target]
                        ?: TutorialFocusAdjustment()
                )
            }
        )

        Text(
            text = buildString {
                appendLine("Copy values:")
                appendLine(target.name)
                appendLine("x = ${adjustment.offsetX.value}dp")
                appendLine("y = ${adjustment.offsetY.value}dp")
                appendLine("scaleX = ${adjustment.scaleX}")
                append("scaleY = ${adjustment.scaleY}")
            },
            color = BudgetPilotTextSecondary,
            fontSize = 11.sp,
            lineHeight = 15.sp
        )
    }
}

@Composable
private fun TutorialCalibrationButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        contentPadding = ButtonDefaults.ContentPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF123B70),
            contentColor = Color.White
        )
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun TutorialMockApp(
    scene: TutorialScene
) {
    if (scene == TutorialScene.Finish) {
        TutorialFinalScene()
        return
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 28.dp)
                .padding(bottom = 96.dp)
        ) {
            TutorialHeader(
                title = if (scene.isAccountsScene()) {
                    "Accounts"
                } else {
                    "Operations"
                },
                subtitle = if (scene.isAccountsScene()) {
                    "Your savings sources"
                } else {
                    "Track your budget activity"
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (scene.isAccountsScene()) {
                TutorialAccountsContent(scene = scene)
            } else {
                TutorialOperationsContent(scene = scene)
            }
        }

        TutorialBottomNavigation(
            selectedTab = if (scene.isAccountsScene()) {
                TutorialTab.Accounts
            } else {
                TutorialTab.Operations
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun TutorialFinalScene() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BudgetPilotScreenGradient)
            .padding(horizontal = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
                .background(BudgetPilotPrimaryCardGradient)
                .budgetPilotOutline(RoundedCornerShape(30.dp))
                .padding(26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "You are ready",
                color = BudgetPilotTextPrimary,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    shadow = BudgetPilotTextShadow
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Enjoy using BudgetPilot. We hope it helps you and your family make calmer, smarter money decisions.",
                color = BudgetPilotTextSecondary,
                fontSize = 17.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TutorialHeader(
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = BudgetPilotTextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    shadow = BudgetPilotTextShadow
                )
            )

            Text(
                text = subtitle,
                color = BudgetPilotTextSecondary,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold
            )
        }

        TutorialAddButton()
    }
}

@Composable
private fun TutorialAddButton() {
    Box(
        modifier = Modifier
            .height(48.dp)
            .width(86.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(BudgetPilotPrimaryCardGradient)
            .budgetPilotOutline(RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Add",
            color = BudgetPilotTextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ColumnScope.TutorialOperationsContent(
    scene: TutorialScene
) {
    if (scene == TutorialScene.OperationsEmpty) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "No operations yet",
            modifier = Modifier.fillMaxWidth(),
            color = BudgetPilotTextSecondary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))
    } else {
        TutorialSummaryCard(
            title = "INCOMES:",
            lines = listOf(
                "PLN  0.00 PLN",
                "USD  0.00 USD",
                "EUR  0.00 EUR"
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TutorialSummaryCard(
            title = "EXPENSES:",
            lines = listOf(
                "PLN  0.00 PLN",
                "USD  84.90 USD",
                "EUR  0.00 EUR"
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        TutorialOperationCard(
            showMenu = scene == TutorialScene.OperationsEditMenu
        )
    }
}

@Composable
private fun TutorialAccountsContent(
    scene: TutorialScene
) {
    TutorialCurrencyCard(
        currency = "EUR",
        amount = if (scene == TutorialScene.AccountsAfterEdit) {
            "2 950.00 EUR"
        } else {
            "1 950.00 EUR"
        },
        accounts = "Cash EUR"
    )

    Spacer(modifier = Modifier.height(12.dp))

    TutorialCurrencyCard(
        currency = "USD",
        amount = if (scene == TutorialScene.AccountsAfterExpense) {
            "1 165.10 USD"
        } else {
            "1 250.00 USD"
        },
        accounts = "BNP Paribas"
    )

    Spacer(modifier = Modifier.height(22.dp))

    TutorialAccountCard(
        name = "Cash EUR",
        type = "Cash",
        amount = if (scene == TutorialScene.AccountsAfterEdit) {
            "2 950.00 EUR"
        } else {
            "1 950.00 EUR"
        }
    )

    Spacer(modifier = Modifier.height(14.dp))

    TutorialAccountCard(
        name = "BNP Paribas",
        type = "Bank account",
        amount = if (scene == TutorialScene.AccountsAfterExpense) {
            "1 165.10 USD"
        } else {
            "1 250.00 USD"
        }
    )
}

@Composable
private fun TutorialSummaryCard(
    title: String,
    lines: List<String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0x14000000))
            .budgetPilotOutline()
            .padding(14.dp)
    ) {
        Text(
            text = title,
            color = BudgetPilotTextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )

        lines.forEach { line ->
            Text(
                text = line,
                modifier = Modifier.fillMaxWidth(),
                color = BudgetPilotAmountNeutral,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun TutorialCurrencyCard(
    currency: String,
    amount: String,
    accounts: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0x14FFFFFF))
            .budgetPilotOutline()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = currency,
                color = BudgetPilotTextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = accounts,
                style = BudgetPilotMetaTextStyle
            )
        }

        Text(
            text = amount,
            color = BudgetPilotTextPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun TutorialOperationCard(
    showMenu: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(BudgetPilotPrimaryCardGradient)
            .budgetPilotOutline(RoundedCornerShape(22.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TutorialCategoryIcon(letter = "G")

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Lidl products",
                    color = BudgetPilotTextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Groceries · BNP Paribas · Expense",
                    style = BudgetPilotMetaTextStyle
                )

                Text(
                    text = "16.06.2026",
                    style = BudgetPilotMetaTextStyle
                )
            }

            Text(
                text = "-84.90 USD",
                color = Color(0xFFFF2F4F),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = if (showMenu) {
                "Edit\nDelete"
            } else {
                "⋮"
            },
            modifier = Modifier.align(Alignment.TopEnd),
            color = BudgetPilotTextPrimary,
            fontSize = if (showMenu) 13.sp else 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun TutorialAccountCard(
    name: String,
    type: String,
    amount: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(BudgetPilotPrimaryCardGradient)
            .budgetPilotOutline(RoundedCornerShape(22.dp))
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TutorialCategoryIcon(letter = name.first().toString())

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                color = BudgetPilotTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = type,
                style = BudgetPilotMetaTextStyle
            )
        }

        Text(
            text = amount,
            color = BudgetPilotTextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun TutorialCategoryIcon(
    letter: String
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x3328A7FF)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            color = BudgetPilotTextPrimary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun TutorialBottomNavigation(
    selectedTab: TutorialTab,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(88.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 22.dp,
                    topEnd = 22.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            )
            .background(Color(0xE6071129)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TutorialNavItem(
            tab = TutorialTab.Accounts,
            selected = selectedTab == TutorialTab.Accounts
        )
        TutorialNavItem(
            tab = TutorialTab.Operations,
            selected = selectedTab == TutorialTab.Operations
        )
        TutorialNavItem(
            tab = TutorialTab.Analytics,
            selected = selectedTab == TutorialTab.Analytics
        )
    }
}

@Composable
private fun TutorialNavItem(
    tab: TutorialTab,
    selected: Boolean
) {
    val itemColor = if (selected) {
        Color(0xFF2BBCFF)
    } else {
        Color(0xFF9AA6C2)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painterResource(tab.iconRes),
            contentDescription = tab.label,
            modifier = Modifier
                .size(24.dp),
            tint = itemColor
        )

        Text(
            text = tab.label,
            color = itemColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun TutorialSpotlightOverlay(
    step: TutorialStep,
    isLastStep: Boolean,
    onNext: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            }
    ) {
        if (step.target != TutorialTarget.Finish) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xDD020714))
            )

            TutorialFocusFrame(target = step.target)
        }

        TutorialMessageCard(
            step = step,
            buttonText = if (isLastStep) {
                "Finish"
            } else {
                "Next"
            },
            onNext = onNext,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 114.dp)
        )
    }
}

@Composable
private fun BoxScope.TutorialFocusFrame(
    target: TutorialTarget,
    adjustment: TutorialFocusAdjustment =
        TutorialFocusAdjustments[target] ?: TutorialFocusAdjustment()
) {
    val modifier = when (target) {
        TutorialTarget.OperationsTab -> {
            TutorialBottomNavigationFocusFrame(
                tab = TutorialTab.Operations,
                adjustment = adjustment
            )
            return
        }

        TutorialTarget.AccountsTab -> {
            TutorialBottomNavigationFocusFrame(
                tab = TutorialTab.Accounts,
                adjustment = adjustment
            )
            return
        }

        TutorialTarget.EmptyOperations -> Modifier
            .align(Alignment.Center)
            .padding(horizontal = 32.dp)
            .fillMaxWidth()
            .height(92.dp)

        TutorialTarget.CurrencySummary -> Modifier
            .align(Alignment.TopCenter)
            .padding(horizontal = 28.dp)
            .padding(top = 124.dp)
            .fillMaxWidth()
            .height(160.dp)

        TutorialTarget.AddButton -> Modifier
            .align(Alignment.TopEnd)
            .padding(top = 42.dp, end = 16.dp)
            .size(width = 100.dp, height = 64.dp)

        TutorialTarget.OperationCard -> Modifier
            .align(Alignment.TopCenter)
            .padding(horizontal = 28.dp)
            .padding(top = 294.dp)
            .fillMaxWidth()
            .height(94.dp)

        TutorialTarget.AccountCards -> Modifier
            .align(Alignment.TopCenter)
            .padding(horizontal = 36.dp)
            .padding(top = 342.dp)
            .fillMaxWidth()
            .height(174.dp)

        TutorialTarget.OperationMenu -> Modifier
            .align(Alignment.TopEnd)
            .padding(top = 326.dp, end = 24.dp)
            .size(width = 96.dp, height = 76.dp)

        TutorialTarget.Finish -> return
    }

    TutorialFocusFrameSurface(
        modifier = modifier,
        adjustment = adjustment
    )
}

@Composable
private fun BoxScope.TutorialBottomNavigationFocusFrame(
    tab: TutorialTab,
    adjustment: TutorialFocusAdjustment
) {
    Row(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(88.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TutorialTab.entries.forEach { currentTab ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(76.dp),
                contentAlignment = Alignment.Center
            ) {
                if (currentTab == tab) {
                    TutorialFocusFrameSurface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .height(68.dp),
                        adjustment = adjustment
                    )
                }
            }
        }
    }
}

@Composable
private fun TutorialFocusFrameSurface(
    modifier: Modifier,
    adjustment: TutorialFocusAdjustment
) {
    Box(
        modifier = modifier
            .offset(
                x = adjustment.offsetX,
                y = adjustment.offsetY
            )
            .graphicsLayer(
                scaleX = adjustment.scaleX,
                scaleY = adjustment.scaleY
            )
            .drawWithContent {
                val cornerRadius = CornerRadius(
                    x = 28.dp.toPx(),
                    y = 28.dp.toPx()
                )

                drawRoundRect(
                    color = Color.Transparent,
                    cornerRadius = cornerRadius,
                    blendMode = BlendMode.Clear
                )

                drawRoundRect(
                    color = Color.White.copy(alpha = 0.88f),
                    cornerRadius = cornerRadius,
                    style = Stroke(width = 2.dp.toPx())
                )
            }
    )
}

@Composable
private fun TutorialMessageCard(
    step: TutorialStep,
    buttonText: String,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(BudgetPilotPrimaryCardGradient)
            .budgetPilotOutline(RoundedCornerShape(24.dp))
            .padding(18.dp)
    ) {
        Text(
            text = step.title,
            color = BudgetPilotTextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                shadow = BudgetPilotTextShadow
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = step.description,
            color = BudgetPilotTextSecondary,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNext,
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF071129)
            )
        ) {
            Text(
                text = buttonText,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private data class TutorialStep(
    val title: String,
    val description: String,
    val scene: TutorialScene,
    val target: TutorialTarget
)

private data class TutorialFocusAdjustment(
    val offsetX: Dp = 0.dp,
    val offsetY: Dp = 0.dp,
    val scaleX: Float = 1f,
    val scaleY: Float = 1f
)

private enum class TutorialScene {
    OperationsEmpty,
    AccountsWithWallets,
    OperationsWithExpense,
    AccountsAfterExpense,
    OperationsEditMenu,
    AccountsAfterEdit,
    Finish;

    fun isAccountsScene(): Boolean {
        return this == AccountsWithWallets ||
            this == AccountsAfterExpense ||
            this == AccountsAfterEdit
    }
}

private enum class TutorialTarget {
    OperationsTab,
    EmptyOperations,
    AccountsTab,
    CurrencySummary,
    AddButton,
    OperationCard,
    AccountCards,
    OperationMenu,
    Finish
}

private enum class TutorialTab(
    val label: String,
    @param:DrawableRes val iconRes: Int
) {
    Accounts(
        label = "Accounts",
        iconRes = R.drawable.ic_credit_card
    ),
    Operations(
        label = "Operations",
        iconRes = R.drawable.ic_import_export
    ),
    Analytics(
        label = "Analytics",
        iconRes = R.drawable.ic_analytics
    )
}

private val TutorialCalibrationPhoneWidth = 390.dp

private val TutorialFocusAdjustments = mapOf(
    TutorialTarget.OperationsTab to TutorialFocusAdjustment(),
    TutorialTarget.EmptyOperations to TutorialFocusAdjustment(
        offsetY = (-12).dp
    ),
    TutorialTarget.AccountsTab to TutorialFocusAdjustment(
        offsetX = 20.dp
    ),
    TutorialTarget.CurrencySummary to TutorialFocusAdjustment(
        offsetY = (-24).dp,
        scaleX = 1.10f,
        scaleY = 1.10f
    ),
    TutorialTarget.AddButton to TutorialFocusAdjustment(
        offsetX = 4.dp,
        offsetY = (-24).dp,
        scaleY = 1.05f
    ),
    TutorialTarget.OperationCard to TutorialFocusAdjustment(
        offsetY = 16.dp,
        scaleX = 1.15f,
        scaleY = 1.20f
    ),
    TutorialTarget.AccountCards to TutorialFocusAdjustment(
        offsetY = (-8).dp,
        scaleX = 1.15f,
        scaleY = 0.65f
    ),
    TutorialTarget.OperationMenu to TutorialFocusAdjustment(
        offsetX = 16.dp,
        offsetY = (-28).dp,
        scaleX = 0.60f,
        scaleY = 0.60f
    ),
    TutorialTarget.Finish to TutorialFocusAdjustment()
)

private val TutorialSteps = listOf(
    TutorialStep(
        title = "Operations workspace",
        description = "This is where your budget events live: expenses, income, transfers, shopping, salary and daily money movement.",
        scene = TutorialScene.OperationsEmpty,
        target = TutorialTarget.OperationsTab
    ),
    TutorialStep(
        title = "Start with accounts",
        description = "There are no operations yet. First add your savings sources, such as cash wallets or bank accounts.",
        scene = TutorialScene.OperationsEmpty,
        target = TutorialTarget.EmptyOperations
    ),
    TutorialStep(
        title = "Your savings sources",
        description = "Accounts represent places where money exists: cash, cards or bank accounts. Balances are grouped by currency.",
        scene = TutorialScene.AccountsWithWallets,
        target = TutorialTarget.AccountsTab
    ),
    TutorialStep(
        title = "Currency summary",
        description = "When you add wallets in different currencies, BudgetPilot keeps totals separated instead of mixing EUR, USD and PLN.",
        scene = TutorialScene.AccountsWithWallets,
        target = TutorialTarget.CurrencySummary
    ),
    TutorialStep(
        title = "Add an operation",
        description = "Tap Add to record an expense, income or transfer. The editor will ask for the account, amount, type and date.",
        scene = TutorialScene.OperationsEmpty,
        target = TutorialTarget.AddButton
    ),
    TutorialStep(
        title = "Operation created",
        description = "Here is a grocery expense paid from BNP Paribas. The operation uses the selected account currency automatically.",
        scene = TutorialScene.OperationsWithExpense,
        target = TutorialTarget.OperationCard
    ),
    TutorialStep(
        title = "Balances stay consistent",
        description = "After an expense, the selected account balance goes down. The currency summary updates with the same rule.",
        scene = TutorialScene.AccountsAfterExpense,
        target = TutorialTarget.AccountCards
    ),
    TutorialStep(
        title = "Edit an operation",
        description = "Open the operation menu and choose Edit. BudgetPilot rolls back the previous balance change before applying the updated operation.",
        scene = TutorialScene.OperationsEditMenu,
        target = TutorialTarget.OperationMenu
    ),
    TutorialStep(
        title = "Updated balances",
        description = "After changing the operation into income, the previous account returns to its original balance and the new account increases.",
        scene = TutorialScene.AccountsAfterEdit,
        target = TutorialTarget.AccountCards
    ),
    TutorialStep(
        title = "Enjoy BudgetPilot",
        description = "You are ready to track family spending, incomes and balances with a cleaner view of your money.",
        scene = TutorialScene.Finish,
        target = TutorialTarget.Finish
    )
)
