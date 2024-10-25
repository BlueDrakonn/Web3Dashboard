package com.example.web3dashboard.ui.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableOpenTarget
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.web3dashboard.R
import com.example.web3dashboard.WalletTopAppBar
import com.example.web3dashboard.data.Token.Token
import com.example.web3dashboard.ui.AppViewModelProvider
import com.example.web3dashboard.ui.navigation.NavigationDestination

object WalletInfoDestination : NavigationDestination {
    override val route = "info"
    override val titleRes = R.string.item_detail_title
    const val walletIdArg = "itemId"
    const val walletAddress = "walletAddress"
    val routeWithArgs = "$route/{$walletIdArg}/{$walletAddress}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletInfoScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WalletInfoViewModel = viewModel(factory = AppViewModelProvider.Factory)

) {
    val walletInfoUiState = viewModel.walletInfoUiState
    val uiStateWallet = viewModel.uiStateWallet.collectAsState()
    val uiStateToken = viewModel.uiStateToken.collectAsState()

    when (walletInfoUiState) {
        is WalletInfoUiState.Loading -> Text(text = "загрузка")
        is WalletInfoUiState.Success -> WalletInfoBody(tokens = walletInfoUiState.tokens)
        is WalletInfoUiState.Error -> Text(text = "хуйня")
    }










//    val uiState = viewModel.uiState.collectAsState()
//    val res by viewModel.textState
//
//    Column {
//        Text(text = uiState.value.walletAddress)
//        Button(onClick = {
//            viewModel.request()
//        }
//        ) {
//            Text(text = "нажми")
//        }
//        Text(text = res)
//
//    }
//    Scaffold(
//        topBar = {
//            WalletTopAppBar(
//                title = uiStateWallet.value.name,
//                canNavigateBack =true,
//                navigateUp = navigateBack
//            )
//        }
//    ) {innerPadding ->
//        WalletInfoBody(
//            modifier = Modifier
//                .padding(
//                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
//                    top = innerPadding.calculateTopPadding(),
//                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
//                )
//        )
//    }


}

@Composable
fun WalletInfoBody(
    modifier: Modifier = Modifier,
    tokens: List<Token>
) {
    Column(modifier = modifier
        .padding(8.dp)) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Token")
            Text(text = "Price")
            Text(text = "Amount")
            Text(text = "USD Value")
        }
        LazyColumn {
            items(items = tokens) {
                 TokenItem(item = it, modifier = Modifier)
            }

        }
    }
}

@Composable
fun TokenItem(item: Token, modifier: Modifier = Modifier) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = item.symbol.toString())
        Text(text = String.format("%.3f", item.price))
        Text(text = String.format("%.5f", item.totalValue))
        Text(text = String.format("%.2f", item.totalValue) + "$")
    }
}