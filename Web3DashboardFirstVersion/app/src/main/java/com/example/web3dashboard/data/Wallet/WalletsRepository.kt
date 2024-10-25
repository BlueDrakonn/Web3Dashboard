package com.example.web3dashboard.data.Wallet

import com.example.web3dashboard.data.Wallet.Wallet
import kotlinx.coroutines.flow.Flow


interface WalletsRepository {

    fun getAllWalletsStream(): Flow<List<Wallet>>


    fun getWalletStream(id: Int): Flow<Wallet?>


    suspend fun insertWallet(wallet: Wallet)


    suspend fun deleteWallet(wallet: Wallet)


    suspend fun updateWallet(wallet: Wallet)
}