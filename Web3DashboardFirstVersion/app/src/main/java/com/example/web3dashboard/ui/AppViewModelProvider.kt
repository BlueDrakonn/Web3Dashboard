package com.example.web3dashboard.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.web3dashboard.WalletApplication
import com.example.web3dashboard.data.Wallet.Wallet
import com.example.web3dashboard.ui.home.HomeViewModel
import com.example.web3dashboard.ui.wallet.WalletAddViewModel
import com.example.web3dashboard.ui.wallet.WalletEditViewModel
import com.example.web3dashboard.ui.wallet.WalletInfoViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
        initializer {
            WalletEditViewModel(
                this.createSavedStateHandle(),
                WalletApplication().container.walletsRepository,
            )
        }
        // Initializer for ItemEntryViewModel
        initializer {
            WalletAddViewModel(WalletApplication().container.walletsRepository)
        }

        // Initializer for ItemDetailsViewModel
        initializer {
            WalletInfoViewModel(
                this.createSavedStateHandle(),
                WalletApplication().container.walletsRepository,
                WalletApplication().container.tokensRepository,
                WalletApplication().container.tokenInfoRepository
            )
        }

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(WalletApplication().container.walletsRepository)
        }
    }
}

fun CreationExtras.WalletApplication(): WalletApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WalletApplication)