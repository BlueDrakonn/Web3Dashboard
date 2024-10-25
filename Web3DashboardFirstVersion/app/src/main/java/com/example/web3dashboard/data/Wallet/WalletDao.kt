package com.example.web3dashboard.data.Wallet

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {

    @Query("SELECT * from wallets")
    fun getAllWallets(): Flow<List<Wallet>>

    @Query("SELECT * from wallets WHERE id = :id")
    fun getWallet(id: Int): Flow<Wallet>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWallet(wallet: Wallet)

    @Update
    suspend fun updateWallet(wallet: Wallet)

    @Delete
    suspend fun deleteWallet(wallet: Wallet)


}

