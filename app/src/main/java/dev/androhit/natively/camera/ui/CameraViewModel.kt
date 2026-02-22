package dev.androhit.natively.camera.ui

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.androhit.natively.camera.data.CameraController
import dev.androhit.natively.data.TextAnalyzer
import dev.androhit.natively.domain.RecognizedText
import dev.androhit.natively.domain.TextScript
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
    private val textAnalyzer: TextAnalyzer,
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

    init {
        viewModelScope.launch {
            textAnalyzer.detectedTextLines.collect {
                _detectedTextLines.value = it
            }
        }
    }

    fun onFeatureSelected(feature: CameraFeature) {
        _selectedFeature.value = feature
        _capturedImage.value = null
        if (feature == CameraFeature.ImageTranslate) {
            _detectedTextLines.value = emptyList()
            cameraController.clearAnalyzer()
        }
    }

    fun translateText(text: String, detectedSource: String?) {
        _translationState.update { it.copy(isLoading = true, translatedText = null, error = null) }
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

    fun translateAllText() {
        _translationState.update { it.copy(isLoading = true) }
        val allText = _detectedTextLines.value.joinToString(" ") { it.text }
        if (allText.isBlank()) {
            _translationState.update { it.copy(error = "No text detected to translate", isLoading = false) }
            return
        }
        
        val detectedSource = _detectedTextLines.value.firstOrNull()?.language
        translateText(allText, detectedSource)
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

    fun analyzeCapturedImage() {
        _capturedImage.value?.let {
            viewModelScope.launch {
                val lines = textAnalyzer.analyzeImage(it)
                _detectedTextLines.value = lines
            }
        }
    }

    fun attachTextAnalyzer() {
        cameraController.setAnalyzer(textAnalyzer.getInstance())
    }

    fun updateScript(script: TextScript) {
        textAnalyzer.updateRecognizer(script)
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

    fun cleanUp() {
        _translationState.update {
            it.copy(
                isLoading = false,
                translatedText = null,
                error = null
            )
        }
        cameraController.clearAnalyzer()
    }

    override fun onCleared() {
        super.onCleared()
        textAnalyzer.close()
        cameraController.detach()
    }
}
