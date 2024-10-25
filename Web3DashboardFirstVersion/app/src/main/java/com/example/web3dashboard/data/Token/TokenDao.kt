package com.example.web3dashboard.data.Token

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.web3dashboard.data.Token.Token
import com.example.web3dashboard.data.Wallet.Wallet
import kotlinx.coroutines.flow.Flow

@Dao
interface TokenDao {

    @Query("SELECT * from tokens WHERE walletId = :walletId")
    fun getTokens(walletId: Int): Flow<List<Token>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertToken(token: Token)

    @Update
    suspend fun updateToken(token: Token)

    @Delete
    suspend fun deleteToken(token: Token)

}