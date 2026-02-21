package dev.androhit.natively.ui.states

data class TranslationState(
    val isLoading: Boolean = false,
    val sourceLanguage: Language = Language.AUTO,
    val targetLanguage: Language = Language.ENGLISH,
    val translatedText: String? = null,
    val error: String? = null,
)