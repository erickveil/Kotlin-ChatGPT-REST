package net.erickveil.kotlinrest

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import net.erickveil.kotlinrest.model.ChatCompletionResponse
import net.erickveil.kotlinrest.model.OpenAIClient
import net.erickveil.kotlinrest.utils.handleResponse
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var apiKeyEditText: EditText
    private lateinit var inputMessageEditText: EditText
    private lateinit var responseTextView: TextView
    private lateinit var sendButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        apiKeyEditText = findViewById(R.id.et_apiKey)
        inputMessageEditText = findViewById(R.id.etm_input)
        responseTextView = findViewById(R.id.tv_response)
        sendButton = findViewById(R.id.but_send)

        // load the API key:
        loadApiKey()

        // Set up the button click listener
        sendButton.setOnClickListener {
            responseTextView.text = "..."
            val apiKey = apiKeyEditText.text.toString()
            val inputMessage = inputMessageEditText.text.toString()
            saveApiKey(apiKey)
            makeApiCall(apiKey, inputMessage)
        }
    }

    private fun makeApiCall(apiKey: String, inputMessage: String) {
        val client = OpenAIClient()

        // Define onResponse callback
        val onResponseCallback: (Response<ChatCompletionResponse>) -> Unit = { response ->
            val messageContent = handleResponse(response)
            responseTextView.text = messageContent
            Log.d("@RESPONSE", messageContent)
        }

        // Define onError callback
        val onErrorCallback: (Throwable) -> Unit = { throwable ->
            responseTextView.text = "Failed to load data: ${throwable.message}"
            Log.e("@ERROR_RESPONSE", "Error calling API", throwable)
        }

        // Call the API
        client.postChatCompletion(inputMessage, apiKey, onResponseCallback, onErrorCallback)
    }

    private fun saveApiKey(apiKey: String) {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("API_KEY", apiKey)
            apply()
        }
    }

    private fun loadApiKey() {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val apiKey = sharedPreferences.getString("API_KEY", "")
        apiKeyEditText.setText(apiKey)
    }
}