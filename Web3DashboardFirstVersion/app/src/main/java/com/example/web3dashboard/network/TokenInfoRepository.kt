package com.example.web3dashboard.network

import android.util.Log
import com.example.web3dashboard.data.RetrofitClient
import com.example.web3dashboard.data.Token.Token
import java.math.BigDecimal
import java.math.BigInteger


interface TokenInfoRepository {

    suspend fun getTokensAddress(walletAddress: String): List<TokenBalance>

    suspend fun getTokensMetadata(walletAddress: String, walletId: Int): List<Token>

}



class NetworkTokenInfoRepository(
    private val alchemyApiService: AlchemyApiService,
    private val coinGeckoApiService: CoinGeckoApiService
):TokenInfoRepository {

    override suspend fun getTokensAddress(walletAddress: String): List<TokenBalance> {

        val alchemyRequest = AlchemyRequest(
            method = "alchemy_getTokenBalances",
            params = listOf(walletAddress)
        )
        //RetrofitClient.setAlchemyBaseUrl("https://polygon-mainnet.g.alchemy.com")
        val response = alchemyApiService.getTokensAddress(alchemyRequest)

        val notNullResult = response.result.tokenBalances.filter { it.tokenBalance != "0x0000000000000000000000000000000000000000000000000000000000000000" }
        return notNullResult
    }

    override suspend fun getTokensMetadata(walletAddress: String, walletId: Int): List<Token> {

        val tokenList = mutableListOf<Token>()

        val networkList = listOf(
            mapOf("pol" to "https://polygon-mainnet.g.alchemy.com"),
            mapOf("eth" to "https://eth-mainnet.g.alchemy.com"),
            mapOf("base" to "https://base-mainnet.g.alchemy.com")
        )


        for (network in networkList) {
            RetrofitClient.setAlchemyBaseUrl(network.values.first())
            val tokenBalanceList = getTokensAddress(walletAddress = walletAddress)


            //во эту шляпу можно реалзиовать через async
            for (tokenBalance in tokenBalanceList) {
                val tokenMetadataResponse = getTokenMetaData(tokenBalance.contractAddress)
                if (hasNullProperties(tokenMetadataResponse.result)) {
                    continue
                }


                Log.d("баланс", "баланс: $tokenMetadataResponse")
                val balance = convertHexToDecimal(
                    tokenBalance.tokenBalance,
                    tokenMetadataResponse.result.decimals
                ).toDouble()
                Log.d("баланс", "все збс")
                val price = getTokenPrice(tokenMetadataResponse.result.symbol ?: "")
                Log.d("баланс", "баланс: $balance")
                Log.d("price", "price: $price")
                val totalValue = balance * price
                if (totalValue > 0.01) {
                    tokenList.add(
                        Token(
                            walletId = walletId,
                            network = network.keys.first(),
                            symbol = tokenMetadataResponse.result.symbol,
                            balance = balance,
                            price = price,
                            totalValue = totalValue,
                            icon = tokenMetadataResponse.result.logo,
                        )
                    )
                }


            }
        }
        return tokenList

    }

    private suspend fun getTokenPrice(fromSymbol: String) : Double{

        val call = coinGeckoApiService.getTokenPrice(
            fromSymbol = fromSymbol
        )
        Log.d("баланс", "МРАЗЬ $call")
        if (call.containsKey("Response")) {
            Log.d("баланс", "МРАЗЬ")
            return 0.0

        }
        val anyTypeResponse = call["USD"] // доступ по ключу
        val doubleValue = anyTypeResponse as? Double
        return doubleValue ?: return 0.0

    }

    private suspend fun getTokenMetaData(address: String): TokenMetadataResponse {

        val request = AlchemyRequest(
            method = "alchemy_getTokenMetadata",
            params = listOf(address)
        )

        return alchemyApiService.getTokenMetadata(request)

    }

    private fun convertHexToDecimal(hexString: String, decimals: Int?): BigDecimal {
        // Убираем префикс "0x", если он присутствует
        val cleanHexString = if (hexString.startsWith("0x")) hexString.substring(2) else hexString

        // Конвертируем шестнадцатеричную строку в BigInteger
        val decimalValue = BigInteger(cleanHexString, 16)

        // Преобразуем BigInteger в BigDecimal для работы с дробными числами
        val bigDecimalValue = BigDecimal(decimalValue)

        // Делаем деление на 10^decimals
        val divisor = BigDecimal.TEN.pow(decimals ?: 1)

        // Возвращаем результат деления
        return bigDecimalValue.divide(divisor)
    }

    private fun hasNullProperties(tokenMetadata: TokenMetadata): Boolean {
        return listOf(
            tokenMetadata.decimals,
            tokenMetadata.logo,
            tokenMetadata.name,
            tokenMetadata.symbol
        ).any { it == null }
    }

}


data class AlchemyRequest(
    val id: Int = 1,
    val jsonrpc: String = "2.0",
    val method: String,
    val params: List<String>
)



data class TokenMetadata(
    val decimals: Int?,
    val logo: String?,
    val name: String?,
    val symbol: String?,

    )



data class TokenResult(
    val address: String,
    val tokenBalances: List<TokenBalance>
)

data class TokenBalance(
    val contractAddress: String,
    val tokenBalance: String
)