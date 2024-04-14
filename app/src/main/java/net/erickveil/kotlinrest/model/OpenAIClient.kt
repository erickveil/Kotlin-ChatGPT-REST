package net.erickveil.kotlinrest.model

import android.util.Log
import net.erickveil.kotlinrest.utils.createPayload
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class OpenAIClient {
    private val baseUrl = "https://api.openai.com"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(OpenAIApiService::class.java)

    fun postChatCompletion(inputMessage: String, apiKey: String,
                           onResponse: (Response<ChatCompletionResponse>) -> Unit,
                           onError: (Throwable) -> Unit) {
        val jsonPayload = createPayload(inputMessage, apiKey)
        val authHeader = "Bearer $apiKey"
        val call = service.createChatCompletion(authHeader, jsonPayload)
        call.enqueue(object : Callback<ChatCompletionResponse> {
            override fun onResponse(call: Call<ChatCompletionResponse>, response: Response<ChatCompletionResponse>) {
                onResponse(response)
            }
            override fun onFailure(call: Call<ChatCompletionResponse>, t: Throwable) {
                onError(t)
            }
        })

    }
}