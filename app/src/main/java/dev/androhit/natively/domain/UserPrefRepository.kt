package dev.androhit.natively.domain

import kotlinx.coroutines.flow.Flow

interface UserPrefRepository {
    val userPreference: Flow<UserPreference>

    suspend fun updateIsFirstLaunch(firstLaunch: Boolean)
    suspend fun updatePreferredScript(script: TextScript)
}
