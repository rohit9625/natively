package dev.androhit.natively.navigation

import androidx.compose.ui.graphics.ImageBitmap
import androidx.navigation3.runtime.NavKey
import dev.androhit.natively.domain.TextScript
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {
    @Serializable
    data object SelectScript: Route, NavKey

    @Serializable
    data object Camera : Route, NavKey

    @Serializable
    data object ViewImage : Route, NavKey
}