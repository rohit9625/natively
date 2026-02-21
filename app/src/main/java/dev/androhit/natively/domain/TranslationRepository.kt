package dev.androhit.natively.domain

import dev.androhit.natively.domain.models.DataError
import dev.androhit.natively.domain.models.Result

interface TranslationRepository {
    suspend fun getTranslation(
        text: String,
        sourceLanguage: String?,
        targetLanguage: String
    ): Result<String, DataError.Network>
}