package dev.androhit.natively.camera.ui

import android.graphics.Bitmap
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.androhit.natively.camera.data.CameraController
import dev.androhit.natively.domain.RecognizedText
import dev.androhit.natively.domain.TranslationRepository
import dev.androhit.natively.domain.models.Result
import dev.androhit.natively.ui.components.CameraFeature
import dev.androhit.natively.ui.states.Language
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

    private val _capturedImage = MutableStateFlow<Bitmap?>(null)
    val capturedImage = _capturedImage.asStateFlow()

    fun onFeatureSelected(feature: CameraFeature) {
        _selectedFeature.value = feature
        _capturedImage.value = null
        if (feature == CameraFeature.ImageTranslate) {
            _detectedTextLines.value = emptyList()
            cameraController.clearAnalyzer()
        }
    }

    fun translateText(text: String, detectedSource: String?) {
        _translationState.update { it.copy(isLoading = true) }
        val targetLanguage = _translationState.value.targetLanguage.code
        val sourceLanguage = if(_translationState.value.sourceLanguage == Language.AUTO) {
            detectedSource
        } else _translationState.value.sourceLanguage.code

        viewModelScope.launch {
            when(val result = translationRepository.getTranslation(text, sourceLanguage, targetLanguage)) {
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

    fun setSourceLanguage(language: Language) {
        _translationState.update { it.copy(sourceLanguage = language) }
    }

    fun setTargetLanguage(language: Language) {
        _translationState.update { it.copy(targetLanguage = language) }
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

    fun capturePhoto(onSuccess: () -> Unit) {
        cameraController.capturePhoto { result ->
            result.onSuccess { bitmap ->
                _capturedImage.value = bitmap
                onSuccess()
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun clearCapturedImage() {
        _capturedImage.value = null
    }
}
