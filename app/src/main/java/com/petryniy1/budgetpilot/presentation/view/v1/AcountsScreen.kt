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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.AccountType
import com.petryniy1.budgetpilot.domain.models.CurrencyCode
import com.petryniy1.budgetpilot.domain.models.Money
import com.petryniy1.budgetpilot.presentation.formatter.formatForDisplay
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F1FA))
            .padding(16.dp)
    ) {
        AccountsHeader(
            accountsCount = accounts.size,
            onAddAccountClick = onAddAccountClick
        )

        AccountActionMessage(actionState = actionState)

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
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Accounts",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF24162F)
            )

            Text(
                text = "$accountsCount items",
                fontSize = 12.sp,
                color = Color(0xFF6D5A78)
            )
        }

        Button(onClick = onAddAccountClick) {
            Text("Add")
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(account) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE7E0EA)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(
                text = account.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = buildString {
                    append(account.type.name)
                    append(" - ")
                    append(account.balance.formatForDisplay())
                },
                fontSize = 13.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = { onDeleteClick(account) }
            ) {
                Text("Delete")
            }
        }
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
            )
        ),
        actionState = AccountActionUiState.Ready,
        onAddAccountClick = {},
        onAccountClick = {},
        onDeleteAccountClick = {}
    )
}
