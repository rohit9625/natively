package dev.androhit.natively.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {
    @Serializable
    data object SelectScript: Route, NavKey

    @Serializable
    data object Camera : Route, NavKey
}