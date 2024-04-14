package net.erickveil.kotlinrest.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIApiService {
    @POST("v1/chat/completions")
    @Headers("Content-Type: application/json")
    fun createChatCompletion(
        @Header("Authorization") authHeader: String,
        @Body payload: ApiPayload
    ): Call<ChatCompletionResponse>
}