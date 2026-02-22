package dev.androhit.natively.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.androhit.natively.domain.TextScript
import dev.androhit.natively.domain.UserPrefRepository
import dev.androhit.natively.domain.UserPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserPrefRepositoryImpl(
    private val dataStore: DataStore<Preferences>
): UserPrefRepository {
    private object PreferencesKeys {
        val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
        val PREFERRED_SCRIPT = stringPreferencesKey("preferred_script")
    }

    override val userPreference: Flow<UserPreference> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { preferences ->
            val isFirstLaunch = preferences[PreferencesKeys.IS_FIRST_LAUNCH]?: false
            val preferredScript = preferences[PreferencesKeys.PREFERRED_SCRIPT]

            UserPreference(
                isFirstLaunch,
                preferredScript = preferredScript?.let {
                    TextScript.valueOf(it)
                } ?: TextScript.Latin
            )
        }

    override suspend fun updateIsFirstLaunch(firstLaunch: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_FIRST_LAUNCH] = firstLaunch
        }
    }

    override suspend fun updatePreferredScript(script: TextScript) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.PREFERRED_SCRIPT] = script.name
        }
    }
}