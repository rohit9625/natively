package dev.androhit.natively.camera.ui

import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.lifecycle.ViewModel
import dev.androhit.natively.camera.data.CameraController
import dev.androhit.natively.domain.RecognizedText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CameraViewModel(
    private val cameraController: CameraController,
): ViewModel() {

    private val _detectedTextLines = MutableStateFlow(emptyList<RecognizedText>())
    val detectedTextLines = _detectedTextLines.asStateFlow()

    fun switchCamera() {
        cameraController.switchCamera()
    }

    fun attachTextAnalyzer(analyzer: MlKitAnalyzer) {
        cameraController.setAnalyzer(analyzer)
    }

    fun onTextDetected(lines: List<RecognizedText>) {
        _detectedTextLines.value = lines
    }

    fun capturePhoto() {
        /** TODO("Capture photo and store it") **/
    }
}