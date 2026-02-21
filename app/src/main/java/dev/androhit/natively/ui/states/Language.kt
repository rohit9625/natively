package dev.androhit.natively.ui.states

enum class Language(
    val code: String,
    val displayName: String
) {
    AUTO("auto", "Detect"),
    ENGLISH("en", "English"),
    HINDI("hi", "हिन्दी"),
    SPANISH("es", "Español"),
    FRENCH("fr", "Français"),
    GERMAN("de", "Deutsch"),
    JAPANESE("ja", "日本語"),
    CHINESE("zh", "中文"),
    KOREAN("ko", "한국어"),
    PORTUGUESE("pt", "Português"),
    ITALIAN("it", "Italiano"),
    RUSSIAN("ru", "Русский");

    companion object {
        fun fromCode(code: String?): Language = entries.find { it.code == code } ?: AUTO
    }
}