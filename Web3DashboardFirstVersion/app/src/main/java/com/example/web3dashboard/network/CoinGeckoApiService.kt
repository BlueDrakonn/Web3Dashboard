package com.example.web3dashboard.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface CoinGeckoApiService {

    @Headers("acceptHeader: application/json")
    @GET("data/price")
    suspend fun getTokenPrice(
        @Query("fsym") fromSymbol: String,
        @Query("tsyms") toSymbols: String = "USD",
        @Header("authorization") apiKey: String ="ba29288d0e80f044747fe6de6f8ab9e11aadcf9bc7865cb5491d64a7561a970a"
    ): Map<String, Any>

}

