package com.example.web3dashboard.data

import android.content.Context
import com.example.web3dashboard.data.Token.OfflineTokensRepository
import com.example.web3dashboard.data.Token.TokensRepository
import com.example.web3dashboard.data.Wallet.OfflineWalletsRepository
import com.example.web3dashboard.data.Wallet.WalletsRepository
import com.example.web3dashboard.network.AlchemyApiService
import com.example.web3dashboard.network.CoinGeckoApiService
import com.example.web3dashboard.network.NetworkTokenInfoRepository
import com.example.web3dashboard.network.TokenInfoRepository
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val walletsRepository: WalletsRepository
    val tokensRepository: TokensRepository
    val tokenInfoRepository: TokenInfoRepository
}


class AppDataContainer(private val context: Context) : AppContainer {

    private val alchemyApiService: AlchemyApiService by lazy {
        RetrofitClient.alchemyApiService
    }

    private val coinGeckoApiService: CoinGeckoApiService by lazy {
        RetrofitClient.coinGeckoApiService
    }

    override val walletsRepository: WalletsRepository by lazy {
        OfflineWalletsRepository(WalletDatabase.getDatabase(context).walletDao())
    }
    override val tokensRepository: TokensRepository by lazy {
        OfflineTokensRepository(WalletDatabase.getDatabase(context).tokenDao())
    }

    override val tokenInfoRepository: TokenInfoRepository by lazy {
        NetworkTokenInfoRepository(alchemyApiService, coinGeckoApiService)
    }


}

object RetrofitClient {
    private var ALCHEMY_BASE_URL = "https://polygon-mainnet.g.alchemy.com"

    private const val COIN_GECKO_BASE_URL = "https://min-api.cryptocompare.com"



    private val baseUrlInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()

        // Получаем оригинальный URL
        val originalUrl = originalRequest.url

        // Создаем новый URL с использованием базового URL и оригинального пути
        val newUrl = ALCHEMY_BASE_URL.toHttpUrlOrNull()!!
            .newBuilder()
            .encodedPath(originalUrl.encodedPath)
            .encodedQuery(originalUrl.encodedQuery)
            .build()

        // Создаем новый запрос с измененным URL
        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(baseUrlInterceptor)
            .build()
    }

    private val alchemyRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(ALCHEMY_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val alchemyApiService: AlchemyApiService by lazy {
        alchemyRetrofit.create(AlchemyApiService::class.java)
    }

    private val coinGeckoRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(COIN_GECKO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val coinGeckoApiService: CoinGeckoApiService by lazy {
        coinGeckoRetrofit.create(CoinGeckoApiService::class.java)
    }



    fun setAlchemyBaseUrl(url: String) {
        ALCHEMY_BASE_URL = url
    }


}
