package com.example.web3dashboard.data.Token

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.web3dashboard.data.Wallet.Wallet

@Entity(
    tableName = "tokens",
    foreignKeys = [ForeignKey(
        entity = Wallet::class,
        parentColumns = ["id"],
        childColumns = ["walletId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Token(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val walletId: Int,
    val network: String = "eth",
    val symbol: String?,
    val balance: Double,
    val price: Double,
    val totalValue: Double,
    val icon: String? = ""
)
