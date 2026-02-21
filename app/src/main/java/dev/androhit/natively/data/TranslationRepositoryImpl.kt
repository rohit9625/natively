package dev.androhit.natively.data

import android.util.Log
import dev.androhit.natively.data.remote.TranslationApi
import dev.androhit.natively.data.remote.TranslationRequest
import dev.androhit.natively.domain.TranslationRepository
import dev.androhit.natively.domain.models.DataError
import dev.androhit.natively.domain.models.Result
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException

class TranslationRepositoryImpl(
    private val api: TranslationApi,
): TranslationRepository {
    override suspend fun getTranslation(
        text: String,
        sourceLanguage: String,
        targetLanguage: String
    ): Result<String, DataError.Network> {
        return try {
            val response = api.translate(TranslationRequest(text, sourceLanguage, targetLanguage))

            if (response.success && response.data != null) {
                Result.Success(response.data.translatedText)
            } else {
                Result.Error(DataError.Network.UNKNOWN)
            }
        } catch (e: UnresolvedAddressException) {
            Result.Error(getErrorFromException(e))
        }
    }

    private fun getErrorFromException(e: Exception): DataError.Network {
        return when(e) {
            is ConnectTimeoutException -> {
                Log.e(TAG, "Server unreachable: ${e.message}")
                DataError.Network.REQUEST_TIMEOUT
            }
            is UnresolvedAddressException -> DataError.Network.NO_INTERNET
            is SerializationException -> {
                Log.e(TAG, "Failed to parse server response", e)
                DataError.Network.SERIALIZATION
            }
            else -> {
                Log.e(TAG, "Unexpected error", e)
                DataError.Network.UNKNOWN
            }
        }
    }

    companion object {
        private const val TAG = "TranslationRepositoryImpl"
    }
}