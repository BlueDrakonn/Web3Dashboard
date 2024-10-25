package com.example.web3dashboard.data.Token

import com.example.web3dashboard.data.Wallet.Wallet
import com.example.web3dashboard.data.Wallet.WalletDao
import kotlinx.coroutines.flow.Flow

class OfflineTokensRepository(private val tokenDao: TokenDao): TokensRepository {

    override fun getTokens(walletId: Int): Flow<List<Token>> = tokenDao.getTokens(walletId)

    override suspend fun insertToken(token: Token) = tokenDao.insertToken(token)

    override suspend fun deleteToken(token: Token) = tokenDao.deleteToken(token)

    override suspend fun updateToken(token: Token) = tokenDao.updateToken(token)
}