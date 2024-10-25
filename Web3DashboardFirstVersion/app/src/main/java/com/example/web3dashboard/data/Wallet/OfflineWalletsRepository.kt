package com.example.web3dashboard.data.Wallet

import com.example.web3dashboard.data.Wallet.Wallet
import com.example.web3dashboard.data.Wallet.WalletDao
import com.example.web3dashboard.data.Wallet.WalletsRepository
import kotlinx.coroutines.flow.Flow

class OfflineWalletsRepository(private val walletDao: WalletDao) : WalletsRepository {
    override fun getAllWalletsStream(): Flow<List<Wallet>> = walletDao.getAllWallets()

    override fun getWalletStream(id: Int): Flow<Wallet?> = walletDao.getWallet(id)

    override suspend fun insertWallet(wallet: Wallet) = walletDao.insertWallet(wallet)

    override suspend fun deleteWallet(wallet: Wallet) = walletDao.deleteWallet(wallet)

    override suspend fun updateWallet(wallet: Wallet) = walletDao.updateWallet(wallet)
}
