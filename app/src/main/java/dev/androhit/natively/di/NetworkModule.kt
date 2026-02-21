package dev.androhit.natively.di

import dev.androhit.natively.BuildConfig
import dev.androhit.natively.data.TranslationRepositoryImpl
import dev.androhit.natively.data.remote.TranslationApi
import dev.androhit.natively.domain.TranslationRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType.Application
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private const val BASE_URL = "http://localhost:8000/api/v1/"

val networkModule = module {
    singleOf(::TranslationApi)
    singleOf(::TranslationRepositoryImpl) bind TranslationRepository::class

    single<HttpClient> {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(json = Json { ignoreUnknownKeys = true })
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 10000L
                connectTimeoutMillis = 10000L
            }

            install(DefaultRequest) {
                contentType(Application.Json)
                url(BASE_URL)
            }

            install(Logging) {
                logger = Logger.ANDROID
                level = if(BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE
            }
        }
    }
}