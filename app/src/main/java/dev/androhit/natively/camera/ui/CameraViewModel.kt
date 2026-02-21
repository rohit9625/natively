package dev.androhit.natively.camera.ui

import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.androhit.natively.camera.data.CameraController
import dev.androhit.natively.domain.RecognizedText
import dev.androhit.natively.domain.TranslationRepository
import dev.androhit.natively.domain.models.Result
import dev.androhit.natively.ui.components.CameraFeature
import dev.androhit.natively.ui.states.TranslationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CameraViewModel(
    private val cameraController: CameraController,
    private val translationRepository: TranslationRepository,
): ViewModel() {

    private val _detectedTextLines = MutableStateFlow(emptyList<RecognizedText>())
    val detectedTextLines = _detectedTextLines.asStateFlow()

    private val _selectedFeature = MutableStateFlow(CameraFeature.LiveTranslate)
    val selectedFeature = _selectedFeature.asStateFlow()

    private val _selectedTextLine = MutableStateFlow<RecognizedText?>(null)
    val selectedTextLine = _selectedTextLine.asStateFlow()

    private val _translationState = MutableStateFlow(TranslationState())
    val translationState = _translationState.asStateFlow()

    fun onFeatureSelected(feature: CameraFeature) {
        _selectedFeature.value = feature
        if (feature == CameraFeature.ImageTranslate) {
            _detectedTextLines.value = emptyList()
            cameraController.clearAnalyzer()
        }
    }

    fun translateText(text: String, sourceLanguage: String?) {
        _translationState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when(val result = translationRepository.getTranslation(text, sourceLanguage, targetLanguage = "hi")) {
                is Result.Error -> {
                    _translationState.update {
                        it.copy(
                            isLoading = false,
                            error = result.error.toString()
                        )
                    }
                }
                is Result.Success -> {
                    _translationState.update {
                        it.copy(
                            isLoading = false,
                            translatedText = result.data
                        )
                    }
                }
            }
        }
    }

    fun setSelectedTextLine(line: RecognizedText?) {
        _translationState.update { it.copy(translatedText = null) }
        _selectedTextLine.value = line
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