package com.example.web3dashboard.network


import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AlchemyApiService {
    @Headers("Content-Type: application/json")
    @POST("/v2/vrPzGCd4HtNUtek8Yjz7qqOKZLELn_ay")
    suspend fun getTokensAddress(@Body request: AlchemyRequest): TokenBalancesResponse

    @Headers("accept: application/json", "content-type: application/json")
    @POST("/v2/vrPzGCd4HtNUtek8Yjz7qqOKZLELn_ay")
    suspend fun getTokenMetadata(@Body request: AlchemyRequest): TokenMetadataResponse


}


data class TokenMetadataResponse(
    val id: Int,
    val jsonrpc: String,
    val result: TokenMetadata
)

data class TokenBalancesResponse(
    val id: Int,
    val jsonrpc: String,
    val result: TokenResult
)