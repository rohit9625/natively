package dev.androhit.natively.data.remote

import dev.androhit.natively.domain.models.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class TranslationApi(
    private val httpClient: HttpClient
) {
    suspend fun translate(requestBody: TranslationRequest): ApiResponse<TranslationResponse> {
        val response = httpClient.post("translate") {
            setBody(requestBody)
        }

        return response.body()
    }
}