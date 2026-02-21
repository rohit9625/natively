package dev.androhit.natively.ui.states

data class TranslationState(
    val isLoading: Boolean = false,
    val translatedText: String? = null,
    val error: String? = null,
)