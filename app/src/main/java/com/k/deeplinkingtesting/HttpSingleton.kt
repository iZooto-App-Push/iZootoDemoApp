package com.k.deeplinkingtesting


import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

object HttpSingleton {
    private val client: OkHttpClient = OkHttpClient()
    private val gson: Gson = Gson()

    suspend fun sendGetRequest(
        url: String,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): OBRecommendationsResponse? = withContext(dispatcher) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseBody = response.body?.string()
            return@withContext gson.fromJson(responseBody, OBRecommendationsResponse::class.java)
        }
    }
}
