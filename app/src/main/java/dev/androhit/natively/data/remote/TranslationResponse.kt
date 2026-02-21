package dev.androhit.natively.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class TranslationResponse(
    val originalText: String,
    val translatedText: String,
    val sourceLanguage: String?,
    val targetLanguage: String?,
)
