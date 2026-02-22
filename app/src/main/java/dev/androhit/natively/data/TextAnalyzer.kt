package dev.androhit.natively.data

import android.content.Context
import androidx.camera.core.ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dev.androhit.natively.domain.RecognizedText
import dev.androhit.natively.domain.TextScript

class TextAnalyzer(
    private val context: Context,
    private val onTextDetected: (List<RecognizedText>) -> Unit
) {
    private val recognizers = mapOf(
        TextScript.Latin to TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS),
        TextScript.Devanagari to TextRecognition.getClient(DevanagariTextRecognizerOptions.Builder().build()),
        TextScript.Chinese to TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build()),
        TextScript.Japanese to TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build()),
        TextScript.Korean to TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build()),
    )

    private var currentScript = TextScript.Latin

    fun getInstance(): MlKitAnalyzer {
        val recognizer = recognizers.getValue(currentScript)
        return MlKitAnalyzer(
            listOf(recognizer),
            COORDINATE_SYSTEM_VIEW_REFERENCED,
            ContextCompat.getMainExecutor(context)
        ) { result ->
            val visionText = result?.getValue(recognizer) ?: return@MlKitAnalyzer

            val lines = visionText.textBlocks.flatMap { block ->
                block.lines.mapNotNull { line ->
                    val confidence = line.elements
                        .mapNotNull { it.confidence }
                        .average()
                        .toFloat()

                    if (confidence < MAX_CONFIDENCE) return@mapNotNull null

                    line.boundingBox?.let { rect ->
                        RecognizedText(
                            text = line.text,
                            boundingBox = rect,
                            language = line.recognizedLanguage
                        )
                    }
                }
            }
            onTextDetected(lines)
        }
    }

    fun updateRecognizer(script: TextScript) {
        currentScript = script
    }

    fun close() {
        recognizers.values.forEach { it.close() }
    }

    companion object {
        private const val MAX_CONFIDENCE = 0.7f
    }
}
