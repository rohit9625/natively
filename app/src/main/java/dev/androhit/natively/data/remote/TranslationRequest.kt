package dev.androhit.natively.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class TranslationRequest(
    val text: String,
    val sourceLanguage: String,
    val targetLanguage: String,
)
