package dev.androhit.natively.data

import android.content.Context
import androidx.camera.core.ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dev.androhit.natively.domain.RecognizedText

class TextAnalyzer(
    private val context: Context,
    private val onTextDetected: (List<RecognizedText>) -> Unit
) {

    private val recognizer =
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun getInstance() = MlKitAnalyzer(
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

    fun close() {
        recognizer.close()
    }

    companion object {
        private const val MAX_CONFIDENCE = 0.7f
    }
}
