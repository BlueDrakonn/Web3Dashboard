package com.example.web3dashboard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.web3dashboard.ui.home.HomeDestination
import com.example.web3dashboard.ui.home.HomeScreen
import com.example.web3dashboard.ui.wallet.WalletAddDestination
import com.example.web3dashboard.ui.wallet.WalletAddScreen
import com.example.web3dashboard.ui.wallet.WalletEditDestination
import com.example.web3dashboard.ui.wallet.WalletEditScreen
import com.example.web3dashboard.ui.wallet.WalletInfoDestination
import com.example.web3dashboard.ui.wallet.WalletInfoScreen

@Composable
fun DashboardNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = HomeDestination.route, modifier = modifier) {
        composable(route= HomeDestination.route) {
            HomeScreen(
                navigateToWalletAdd = {navController.navigate(WalletAddDestination.route)},
                navigateToWalletEdit = {navController.navigate("${WalletEditDestination.route}/${it}")},
                navigateToWalletInfo = {walletId, walletAddress ->
                    navController.navigate("${WalletInfoDestination.route}/${walletId}/${walletAddress}")}


            )
        }
        composable(route = WalletAddDestination.route) {
            WalletAddScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = {navController.navigateUp()})
        }
        composable(
            route = WalletEditDestination.routeWithArgs,
            arguments = listOf(navArgument(WalletEditDestination.walletIdArg) {
                type = NavType.IntType
            })
        ) {
            WalletEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = {navController.navigateUp()})
        }
        composable(
            route = WalletInfoDestination.routeWithArgs,
            arguments = listOf(navArgument(WalletInfoDestination.walletIdArg){
                type = NavType.IntType
            })
        ) {
            WalletInfoScreen(
                navigateBack = {navController.popBackStack()}
            )
        }




    }
}