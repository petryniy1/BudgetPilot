package com.petryniy1.budgetpilot.presentation.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.AccountType
import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.domain.models.CurrencyCode
import com.petryniy1.budgetpilot.domain.models.Money
import com.petryniy1.budgetpilot.domain.models.OperationType
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotAccentBlue
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotScreenGradient
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextSecondary
import com.petryniy1.budgetpilot.presentation.feature.accounts.AccountsScreen
import com.petryniy1.budgetpilot.presentation.feature.analytics.AnalyticsScreen
import com.petryniy1.budgetpilot.presentation.feature.operations.BudgetOperationsScreen
import com.petryniy1.budgetpilot.presentation.feature.tutorial.GuidedTutorialFocusCalibrationScreen
import java.time.LocalDate

@Preview(
    name = "Tutorial focus calibration - Interactive Mode",
    widthDp = 580,
    heightDp = 844,
    showBackground = true
)
@Composable
private fun GuidedTutorialFocusCalibrationPreview() {
    GuidedTutorialFocusCalibrationScreen()
}

@Preview(
    name = "Accounts with bottom menu - phone",
    widthDp = 390,
    heightDp = 844,
    showBackground = true
)
@Preview(
    name = "Accounts with bottom menu - tablet landscape",
    widthDp = 900,
    heightDp = 600,
    showBackground = true
)
@Composable
private fun AccountsScreenWithBottomMenuPreview() {
    BudgetPilotPreviewScaffold(
        selectedTab = BudgetPilotPreviewTab.Accounts
    ) {
        AccountsScreen(
            accounts = previewAccounts,
            onAddAccountClick = {},
            onAccountClick = {},
            onDeleteAccountClick = {}
        )
    }
}

@Preview(
    name = "Operations with bottom menu - phone",
    widthDp = 390,
    heightDp = 844,
    showBackground = true
)
@Preview(
    name = "Operations with bottom menu - tablet landscape",
    widthDp = 900,
    heightDp = 600,
    showBackground = true
)
@Composable
private fun OperationsScreenWithBottomMenuPreview() {
    BudgetPilotPreviewScaffold(
        selectedTab = BudgetPilotPreviewTab.Operations
    ) {
        BudgetOperationsScreen(
            operations = previewOperations,
            accounts = previewAccounts,
            onAddOperationClick = {},
            onOperationClick = {},
            onDeleteOperationClick = {}
        )
    }
}

@Preview(
    name = "Analytics with bottom menu - phone",
    widthDp = 390,
    heightDp = 844,
    showBackground = true
)
@Preview(
    name = "Analytics with bottom menu - tablet landscape",
    widthDp = 900,
    heightDp = 600,
    showBackground = true
)
@Composable
private fun AnalyticsScreenWithBottomMenuPreview() {
    BudgetPilotPreviewScaffold(
        selectedTab = BudgetPilotPreviewTab.Analytics
    ) {
        AnalyticsScreen()
    }
}

@Composable
private fun BudgetPilotPreviewScaffold(
    selectedTab: BudgetPilotPreviewTab,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BudgetPilotScreenGradient)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Transparent)
        ) {
            content()
        }

        BudgetPilotPreviewBottomMenu(
            selectedTab = selectedTab
        )
    }
}

@Composable
private fun BudgetPilotPreviewBottomMenu(
    selectedTab: BudgetPilotPreviewTab
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 22.dp,
                    topEnd = 22.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp,
                )
            )
            .background(Color.Black),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BudgetPilotPreviewBottomMenuItem(
            tab = BudgetPilotPreviewTab.Accounts,
            selected = selectedTab == BudgetPilotPreviewTab.Accounts
        )

        BudgetPilotPreviewBottomMenuItem(
            tab = BudgetPilotPreviewTab.Operations,
            selected = selectedTab == BudgetPilotPreviewTab.Operations
        )

        BudgetPilotPreviewBottomMenuItem(
            tab = BudgetPilotPreviewTab.Analytics,
            selected = selectedTab == BudgetPilotPreviewTab.Analytics
        )
    }
}

@Composable
private fun BudgetPilotPreviewBottomMenuItem(
    tab: BudgetPilotPreviewTab,
    selected: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = tab.iconText,
            color = if (selected) BudgetPilotAccentBlue else BudgetPilotTextSecondary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = tab.label,
            color = if (selected) BudgetPilotAccentBlue else BudgetPilotTextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private enum class BudgetPilotPreviewTab(
    val label: String,
    val iconText: String
) {
    Accounts(
        label = "Accounts",
        iconText = "A"
    ),
    Operations(
        label = "Operations",
        iconText = "O"
    ),
    Analytics(
        label = "Analytics",
        iconText = "I"
    )
}

private val previewAccounts = listOf(
    Account(
        id = 1,
        name = "Cash EUR",
        type = AccountType.CASH,
        balance = Money(
            amountMinor = 125_000,
            currency = CurrencyCode.EUR
        )
    ),
    Account(
        id = 2,
        name = "BNP Paribas",
        type = AccountType.BANK_ACCOUNT,
        balance = Money(
            amountMinor = 250_050,
            currency = CurrencyCode.USD
        )
    ),
    Account(
        id = 3,
        name = "Main PLN wallet",
        type = AccountType.CASH,
        balance = Money(
            amountMinor = 520_000,
            currency = CurrencyCode.PLN
        )
    )
)

private val previewOperations = listOf(
    BudgetOperation(
        id = 1,
        accountId = 2,
        title = "Lidl products",
        amount = Money(
            amountMinor = 8_490,
            currency = CurrencyCode.USD
        ),
        type = OperationType.EXPENSE,
        date = LocalDate.of(2026, 6, 16),
        comment = "Weekly groceries"
    ),
    BudgetOperation(
        id = 2,
        accountId = 1,
        title = "Salary",
        amount = Money(
            amountMinor = 513_556_000,
            currency = CurrencyCode.EUR
        ),
        type = OperationType.INCOME,
        date = LocalDate.of(2026, 6, 15),
        comment = "Monthly income"
    ),
    BudgetOperation(
        id = 3,
        accountId = 3,
        title = "Fuel station",
        amount = Money(
            amountMinor = 24_550,
            currency = CurrencyCode.PLN
        ),
        type = OperationType.EXPENSE,
        date = LocalDate.of(2026, 6, 14),
        comment = "Car expenses"
    )
)
