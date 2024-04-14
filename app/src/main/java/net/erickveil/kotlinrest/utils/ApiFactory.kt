package net.erickveil.kotlinrest.utils

import android.util.Log
import com.google.gson.Gson
import net.erickveil.kotlinrest.model.ApiPayload
import net.erickveil.kotlinrest.model.ChatCompletionResponse
import net.erickveil.kotlinrest.model.Message
import retrofit2.Response

fun createPayload(inputMsg: String, apiKey: String): ApiPayload {
    val systemMessage = Message("system", "You are a helpful assistant.")
    val userMessage = Message("user", inputMsg)
    return ApiPayload("gpt-3.5-turbo", listOf(systemMessage, userMessage))
}

fun handleResponse(response: Response<ChatCompletionResponse>): String {
    if (response.isSuccessful) {
        response.body()?.let { completionResponse ->
            val firstMessageContent = completionResponse.choices.firstOrNull()?.message?.content
            Log.d("@MSG", (firstMessageContent ?: "NO MSG??"))
            return firstMessageContent ?: "No message content available."
        } ?: "Response body is null."
    }
    Log.e("@ERROR", "Error: ${response.errorBody()?.string()}")
    return "Error: ${response.errorBody()?.string()}"
}