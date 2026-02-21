package dev.androhit.natively.domain.models

interface DataError: Error {
    enum class Network: DataError {
        REQUEST_TIMEOUT,
        UNAUTHORIZED,
        CONFLICT,
        BAD_REQUEST,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN
    }
    enum class Local: DataError {
        DISK_FULL,
        UNKNOWN
    }
}