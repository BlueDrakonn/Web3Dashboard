package com.example.web3dashboard.ui.wallet

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.web3dashboard.data.Token.Token
import com.example.web3dashboard.data.Token.TokensRepository
import com.example.web3dashboard.data.Wallet.Wallet
import com.example.web3dashboard.data.Wallet.WalletsRepository
import com.example.web3dashboard.network.NetworkTokenInfoRepository
import com.example.web3dashboard.network.TokenInfoRepository
import com.example.web3dashboard.ui.home.HomeUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.io.IOException


sealed interface WalletInfoUiState {
    data class Success(val tokens: List<Token>) : WalletInfoUiState
    object Error : WalletInfoUiState
    object Loading : WalletInfoUiState
}


class WalletInfoViewModel(
    savedStateHandle: SavedStateHandle,
    walletsRepository: WalletsRepository,
    tokensRepository: TokensRepository,
    private val tokenInfoRepository: TokenInfoRepository) :ViewModel() {

    private val walletId: Int = checkNotNull(savedStateHandle[WalletInfoDestination.walletIdArg])
    private val walletAddress: String = checkNotNull(savedStateHandle[WalletInfoDestination.walletAddress])

//    private val walletAddress: String =
//        walletsRepository.getWalletStream(walletId)
//            .filterNotNull()
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//                initialValue = Wallet()
//            ).value.walletAddress

    var walletInfoUiState: WalletInfoUiState by mutableStateOf(WalletInfoUiState.Loading)
        private set

    init {
        getTokensInfo(walletId, walletAddress)
    }

    val uiStateWallet: StateFlow<Wallet> =
        walletsRepository.getWalletStream(walletId)
            .filterNotNull()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = Wallet()
            )

    val uiStateToken: StateFlow<TokenUiState> =
        tokensRepository.getTokens(walletId)
            .filterNotNull()
            .map { TokenUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = TokenUiState()
            )


    private fun getTokensInfo(walletId: Int, walletAddress: String) {
        viewModelScope.launch {
            walletInfoUiState = WalletInfoUiState.Loading
            walletInfoUiState = try {
                withContext(Dispatchers.IO) {
                    WalletInfoUiState.Success(
                        tokenInfoRepository.getTokensMetadata(walletId = walletId, walletAddress = walletAddress)
                    )
                } } catch (e: IOException) {
                Log.e(TAG, "HttpException occurred: ${e.message}", e)
                WalletInfoUiState.Error
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException occurred: ${e.message}", e)
                WalletInfoUiState.Error
            }
        }

    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class TokenUiState(val tokenList: List<Token> = listOf())


