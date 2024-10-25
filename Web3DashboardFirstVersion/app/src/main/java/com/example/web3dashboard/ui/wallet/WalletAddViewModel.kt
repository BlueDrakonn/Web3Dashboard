package com.example.web3dashboard.ui.wallet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

import com.example.web3dashboard.data.Wallet.Wallet
import com.example.web3dashboard.data.Wallet.WalletsRepository

class WalletAddViewModel(private val walletsRepository: WalletsRepository): ViewModel() {

    var walletUiState by mutableStateOf(WalletUiState())
        private set

    fun updateUiState(Wallet: Wallet) {
        walletUiState =
            WalletUiState(Wallet = Wallet, isEntryValid = validateInput(Wallet))
    }

    /**
     * Inserts an [Item] in the Room database
     */
    suspend fun saveWallet() {
        if (validateInput()) {
            walletsRepository.insertWallet(walletUiState.Wallet)
        }
    }

    private fun validateInput(uiState: Wallet = walletUiState.Wallet): Boolean {
        return with(uiState) {
            name.isNotBlank() && walletAddress.isNotBlank()
        }
    }

}

data class WalletUiState(
    val Wallet: Wallet = Wallet(),
    val isEntryValid: Boolean = false
)


