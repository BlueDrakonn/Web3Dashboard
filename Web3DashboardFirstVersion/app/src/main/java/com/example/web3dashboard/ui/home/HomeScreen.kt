package com.example.web3dashboard.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.web3dashboard.ui.navigation.NavigationDestination
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.web3dashboard.R
import com.example.web3dashboard.WalletTopAppBar
import com.example.web3dashboard.data.Wallet.Wallet
import com.example.web3dashboard.ui.AppViewModelProvider

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToWalletAdd: () -> Unit,
    navigateToWalletEdit: (Int) -> Unit,
    navigateToWalletInfo: (Int, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar ={
            WalletTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToWalletAdd,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .padding(
                        end = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateEndPadding(LocalLayoutDirection.current)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.item_entry_title)
                )
            }
        }
    ) {innerPadding ->
        HomeBody(
            walletList = homeUiState.walletList,
            onItemClick = navigateToWalletInfo,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
        )
    }

}


@Composable
//151 было item.id
private fun HomeBody(
    walletList : List<Wallet>,
    onItemClick: (Int, String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (walletList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_item_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            WalletList(
                walletList = walletList,
                onItemClick = {walletId, walletAddress -> onItemClick(walletId,walletAddress)} ,
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }

}

@Composable
private fun WalletList(
    walletList: List<Wallet>,
    onItemClick: (Int, String) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
){
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(walletList, key = { it.id }) { wallet ->
            WalletItem(wallet = wallet,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onItemClick(wallet.id, wallet.walletAddress) })
        }
    }
}


@Composable
private fun WalletItem(
    wallet: Wallet, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Text(
                text = wallet.name,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge
                )

            Text(
                text = stringResource(R.string.in_stock, wallet.walletAddress),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}



