package dev.androhit.natively.camera.ui

import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.lifecycle.ViewModel
import dev.androhit.natively.camera.data.CameraController
import dev.androhit.natively.domain.RecognizedText
import dev.androhit.natively.ui.components.CameraFeature
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CameraViewModel(
    private val cameraController: CameraController,
): ViewModel() {

    private val _detectedTextLines = MutableStateFlow(emptyList<RecognizedText>())
    val detectedTextLines = _detectedTextLines.asStateFlow()

    private val _selectedFeature = MutableStateFlow(CameraFeature.LiveTranslate)
    val selectedFeature = _selectedFeature.asStateFlow()

    fun onFeatureSelected(feature: CameraFeature) {
        _selectedFeature.value = feature
        if (feature == CameraFeature.ImageTranslate) {
            _detectedTextLines.value = emptyList()
            cameraController.clearAnalyzer()
        }
    }

    fun switchCamera() {
        cameraController.switchCamera()
    }

    fun attachTextAnalyzer(analyzer: MlKitAnalyzer) {
        cameraController.setAnalyzer(analyzer)
    }

    fun onTextDetected(lines: List<RecognizedText>) {
        if (_selectedFeature.value == CameraFeature.LiveTranslate) {
            _detectedTextLines.value = lines
        }
    }

    fun capturePhoto() {
        /** TODO("Capture photo and store it") **/
    }
}