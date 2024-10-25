package com.example.web3dashboard.ui.wallet

import androidx.compose.runtime.Composable
import com.example.web3dashboard.R
import com.example.web3dashboard.ui.navigation.NavigationDestination

object WalletEditDestination : NavigationDestination {
    override val route = "edit"
    override val titleRes = R.string.edit_item_title
    const val walletIdArg = "walletId"
    val routeWithArgs = "$route/{$walletIdArg}"

}

@Composable
fun WalletEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
) {}