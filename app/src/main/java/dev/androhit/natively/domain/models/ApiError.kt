package dev.androhit.natively.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val code: String,
    val message: String,
)
