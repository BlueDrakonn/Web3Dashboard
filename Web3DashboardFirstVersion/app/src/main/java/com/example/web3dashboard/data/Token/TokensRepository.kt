package com.example.web3dashboard.data.Token

import com.example.web3dashboard.data.Wallet.Wallet
import kotlinx.coroutines.flow.Flow

interface TokensRepository {

    fun getTokens(walletId: Int): Flow<List<Token>>


    suspend fun insertToken(token: Token)


    suspend fun deleteToken(token: Token)


    suspend fun updateToken(token: Token)
}