package net.erickveil.kotlinrest.model

data class Message(val role: String, val content: String)
data class ApiPayload(val model: String, val messages: List<Message>)
