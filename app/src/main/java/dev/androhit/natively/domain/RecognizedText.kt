package dev.androhit.natively.domain

import android.graphics.Rect

data class RecognizedText(
    val text: String,
    val boundingBox: Rect,
    val language: String? = null,
)
