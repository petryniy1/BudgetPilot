package com.petryniy1.budgetpilot.presentation.view.v1

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.AccountType
import com.petryniy1.budgetpilot.domain.models.CurrencyCode
import com.petryniy1.budgetpilot.domain.models.Money
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotAmountNeutral
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotCurrencyFrameOverlay
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotPrimaryCardGradient
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotScreenGradient
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotSoftTextShadow
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextPrimary
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextSecondary
import com.petryniy1.budgetpilot.presentation.design.budgetPilotOutline
import com.petryniy1.budgetpilot.presentation.design.components.GradientAddButton
import com.petryniy1.budgetpilot.presentation.formatter.formatForDisplay
import com.petryniy1.budgetpilot.presentation.mapper.toCurrencyGroups
import com.petryniy1.budgetpilot.presentation.uiModels.AccountCurrencyGroupUiModel
import com.petryniy1.budgetpilot.presentation.uiState.AccountActionError
import com.petryniy1.budgetpilot.presentation.uiState.AccountActionUiState

@Composable
fun AccountsScreen(
    accounts: List<Account>,
    actionState: AccountActionUiState,
    onAddAccountClick: () -> Unit,
    onAccountClick: (Int) -> Unit,
    onDeleteAccountClick: (Int) -> Unit
) {
    var accountPendingDelete by remember {
        mutableStateOf<Account?>(null)
    }

    accountPendingDelete?.let { account ->
        DeleteAccountDialog(
            account = account,
            onConfirm = { accountId ->
                accountPendingDelete = null
                onDeleteAccountClick(accountId)
            },
            onDismiss = { accountPendingDelete = null }
        )
    }

    val currencyGroups = remember(accounts) {
        accounts.toCurrencyGroups()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BudgetPilotScreenGradient)
            .padding(16.dp)
    ) {
        AccountsHeader(
            accountsCount = accounts.size,
            onAddAccountClick = onAddAccountClick
        )

        AccountActionMessage(actionState = actionState)

        Spacer(modifier = Modifier.height(8.dp))

        CurrencyGroupsSection(
            currencyGroups = currencyGroups
        )

        Spacer(modifier = Modifier.height(12.dp))

        AccountsContent(
            accounts = accounts,
            onAccountClick = onAccountClick,
            onDeleteAccountClick = { account ->
                accountPendingDelete = account
            }
        )
    }
}

@Composable
private fun AccountsHeader(
    accountsCount: Int,
    onAddAccountClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Accounts",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "$accountsCount accounts",
                modifier = Modifier.weight(1f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = Color(0xFFD6E4FF),
                textAlign = TextAlign.Center
            )

            GradientAddButton(
                onClick = onAddAccountClick
            )
        }
    }
}

@Composable
fun CurrencyGroupsSection(
    currencyGroups: List<AccountCurrencyGroupUiModel>
) {
    if (currencyGroups.isEmpty()) return

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        currencyGroups.forEach { group ->
            CurrencyGroupCard(group = group)
        }
    }
}

@Composable
private fun CurrencyGroupCard(
    group: AccountCurrencyGroupUiModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .budgetPilotOutline(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BudgetPilotCurrencyFrameOverlay)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = group.currency.name,
                    color = BudgetPilotTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = group.accounts.joinToString(" · ") { account ->
                        account.name
                    },
                    color = BudgetPilotTextSecondary,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            Text(
                text = group.totalBalance.formatForDisplay(),
                color = BudgetPilotTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun AccountsContent(
    accounts: List<Account>,
    onAccountClick: (Int) -> Unit,
    onDeleteAccountClick: (Account) -> Unit
) {
    val listState = rememberLazyListState()

    if (accounts.isEmpty()) {
        EmptyAccountsState()
    } else {
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                items = accounts,
                key = { account -> account.id }
            ) { account ->
                AccountItem(
                    account = account,
                    onClick = { clickedAccount ->
                        onAccountClick(clickedAccount.id)
                    },
                    onDeleteClick = onDeleteAccountClick
                )
            }
        }
    }
}

@Composable
private fun EmptyAccountsState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No accounts yet",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF6D5A78)
        )
    }
}

@Composable
private fun AccountItem(
    account: Account,
    onClick: (Account) -> Unit,
    onDeleteClick: (Account) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(136.dp)
            .budgetPilotOutline()
            .clickable { onClick(account) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BudgetPilotPrimaryCardGradient)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 92.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            color = Color(0xFF002967),
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = account.type.toBadgeText(),
                        color = BudgetPilotTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.size(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = account.name,
                        color = BudgetPilotTextSecondary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = account.type.toDisplayText(),
                        color = BudgetPilotTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .wrapContentSize(Alignment.TopEnd)
            ) {
                IconButton(
                    modifier = Modifier
                        .size(24.dp)
                        .offset(x = 8.dp, y = (-8).dp),
                    onClick = { menuExpanded = true }
                ) {
                    Text(
                        text = "⋮",
                        color = BudgetPilotTextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = {
                            menuExpanded = false
                            onClick(account)
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = {
                            menuExpanded = false
                            onDeleteClick(account)
                        }
                    )
                }
            }

            Text(
                text = account.balance.formatForDisplay(),
                modifier = Modifier.align(Alignment.BottomEnd),
                color = BudgetPilotAmountNeutral,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    shadow = BudgetPilotSoftTextShadow
                )
            )
        }
    }
}

private fun AccountType.toDisplayText(): String {
    return when (this) {
        AccountType.CASH -> "Cash"
        AccountType.BANK_ACCOUNT -> "Bank account"
    }
}

private fun AccountType.toBadgeText(): String {
    return when (this) {
        AccountType.CASH -> "$"
        AccountType.BANK_ACCOUNT -> "B"
    }
}

@Composable
private fun DeleteAccountDialog(
    account: Account,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Delete account") },
        text = { Text(text = "Are you sure you want to delete ${account.name}?") },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(account.id) }
            ) {
                Text(text = "Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}

@Composable
private fun AccountActionMessage(
    actionState: AccountActionUiState
) {
    when (actionState) {
        AccountActionUiState.Ready -> Unit
        AccountActionUiState.Loading -> Text("Loading...")
        AccountActionUiState.Success -> Text("Account saved")
        is AccountActionUiState.Error -> Text(actionState.reason.toMessage())
    }
}

private fun AccountActionError.toMessage(): String {
    return when (this) {
        AccountActionError.AccountNotFound -> "Account not found"
        AccountActionError.DuplicateAccountName -> "Account name already exists"
        AccountActionError.EmptyName -> "Account name cannot be empty"
        AccountActionError.NegativeBalance -> "Balance cannot be negative"
        AccountActionError.Unexpected -> "Unexpected error"
    }
}

@Preview(showBackground = true)
@Composable
private fun AccountsScreenPreview() {
    AccountsScreen(
        accounts = listOf(
            Account(
                id = 1,
                name = "Cash PLN",
                type = AccountType.CASH,
                balance = Money(
                    amountMinor = 125000,
                    currency = CurrencyCode.PLN
                )
            ),
            Account(
                id = 2,
                name = "Santander",
                type = AccountType.BANK_ACCOUNT,
                balance = Money(
                    amountMinor = 250050,
                    currency = CurrencyCode.PLN
                )
            ),
            Account(
                id = 3,
                name = "Santander_USD",
                type = AccountType.BANK_ACCOUNT,
                balance = Money(
                    amountMinor = 250050,
                    currency = CurrencyCode.USD
                )
            ),
            Account(
                id = 7,
                name = "Santander_USD",
                type = AccountType.BANK_ACCOUNT,
                balance = Money(
                    amountMinor = 250023232350,
                    currency = CurrencyCode.USD
                )
            ),
            Account(
                id = 8,
                name = "Santander_USD",
                type = AccountType.BANK_ACCOUNT,
                balance = Money(
                    amountMinor = 250050,
                    currency = CurrencyCode.USD
                )
            ),
            Account(
                id = 9,
                name = "Santander_USD",
                type = AccountType.BANK_ACCOUNT,
                balance = Money(
                    amountMinor = 250343443050,
                    currency = CurrencyCode.USD
                )
            ),
            Account(
                id = 4,
                name = "Santander_EUR",
                type = AccountType.BANK_ACCOUNT,
                balance = Money(
                    amountMinor = 25333334550,
                    currency = CurrencyCode.EUR
                )
            )
        ),
        actionState = AccountActionUiState.Ready,
        onAddAccountClick = {},
        onAccountClick = {},
        onDeleteAccountClick = {}
    )
}
