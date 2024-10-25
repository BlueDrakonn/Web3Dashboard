package com.example.web3dashboard.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.web3dashboard.data.Token.Token
import com.example.web3dashboard.data.Token.TokenDao
import com.example.web3dashboard.data.Wallet.Wallet
import com.example.web3dashboard.data.Wallet.WalletDao

@Database(entities = [Wallet::class, Token::class], version = 4, exportSchema = false)
abstract class WalletDatabase : RoomDatabase() {

    abstract fun walletDao(): WalletDao
    abstract fun tokenDao(): TokenDao

    companion object {
        @Volatile
        private var Instance: WalletDatabase? = null

        fun getDatabase(context: Context): WalletDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, WalletDatabase::class.java, "wallet_database")
                    /**
                     * Setting this option in your app's database builder means that Room
                     * permanently deletes all data from the tables in your database when it
                     * attempts to perform a migration with no defined migration path.
                     */
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}