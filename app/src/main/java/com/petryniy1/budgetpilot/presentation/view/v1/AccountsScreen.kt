package com.petryniy1.budgetpilot.presentation.view.v1

import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.AccountType
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotAmountNeutral
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotMetaTextStyle
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotPrimaryCardGradient
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotScreenGradient
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextPrimary
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextSecondary
import com.petryniy1.budgetpilot.presentation.design.BudgetPilotTextShadow
import com.petryniy1.budgetpilot.presentation.design.budgetPilotOutline
import com.petryniy1.budgetpilot.presentation.design.components.BudgetPilotDialog
import com.petryniy1.budgetpilot.presentation.design.components.GradientAddButton
import com.petryniy1.budgetpilot.presentation.formatter.formatForDisplay
import com.petryniy1.budgetpilot.presentation.mapper.toCurrencyGroups
import com.petryniy1.budgetpilot.presentation.uiModels.AccountCurrencyGroupUiModel

@Composable
fun AccountsScreen(
    accounts: List<Account>,
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

        Spacer(modifier = Modifier.height(8.dp))

        AccountsContent(
            accounts = accounts,
            currencyGroups = currencyGroups,
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
                color = BudgetPilotTextPrimary,
                style = TextStyle(
                    shadow = BudgetPilotTextShadow
                )
            )

            Text(
                text = "$accountsCount accounts in",
                modifier = Modifier.weight(1f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = Color(0xFFD6E4FF),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    shadow = BudgetPilotTextShadow
                )
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
            ExInGroupCard(group = group)
        }
    }
}

@Composable
private fun ExInGroupCard(
    group: AccountCurrencyGroupUiModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .budgetPilotOutline(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = BudgetPilotTextShadow
                    )
                )
            }

            Text(
                text = group.totalBalance.formatForDisplay(),
                color = BudgetPilotTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                style = TextStyle(
                    shadow = BudgetPilotTextShadow
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = group.accounts.joinToString(" · ") { account ->
                    account.name
                },
                style = BudgetPilotMetaTextStyle
            )
        }
    }
}

@Composable
private fun AccountsContent(
    accounts: List<Account>,
    currencyGroups: List<AccountCurrencyGroupUiModel>,
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
            item(
                key = "currency_groups"
            ) {
                CurrencyGroupsSection(
                    currencyGroups = currencyGroups
                )
            }

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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                        color = BudgetPilotTextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(
                            shadow = BudgetPilotTextShadow
                        )
                    )

                    Text(
                        text = account.type.toDisplayText(),
                        style = BudgetPilotMetaTextStyle
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
                    shadow = BudgetPilotTextShadow
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
    BudgetPilotDialog(
        title = "Delete account",
        confirmText = "Delete",
        dismissText = "Cancel",
        isDestructive = true,
        onConfirm = {
            onConfirm(account.id)
        },
        onDismiss = onDismiss
    ) {
        Text(
            text = "Are you sure you want to delete ${account.name}?",
            color = BudgetPilotTextSecondary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}