package dev.androhit.natively.domain

data class UserPreference(
    val isFirstLaunch: Boolean = true,
    val preferredScript: TextScript = TextScript.Latin,
)
