package com.example.web3dashboard

import android.app.Application
import android.content.Context
import com.example.web3dashboard.data.AppContainer
import com.example.web3dashboard.data.AppDataContainer

class WalletApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}