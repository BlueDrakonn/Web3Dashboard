package com.example.web3dashboard.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.web3dashboard.data.Wallet.Wallet
import com.example.web3dashboard.data.Wallet.WalletsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(walletsRepository: WalletsRepository): ViewModel(){

    val homeUiState: StateFlow<HomeUiState> = walletsRepository.getAllWalletsStream().map { HomeUiState(it) }
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = HomeUiState()
    )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

data class HomeUiState(val walletList: List<Wallet> = listOf())