package com.example.web3dashboard.data.Wallet

import androidx.room.Entity
import androidx.room.PrimaryKey

//не уверен что корректно ставить значеняи по умолчанию у name и walletAddress
@Entity(tableName = "wallets")
data class Wallet(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val walletAddress: String = "",
)