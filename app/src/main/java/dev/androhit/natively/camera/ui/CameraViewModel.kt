package dev.androhit.natively.camera.ui

import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.lifecycle.ViewModel
import dev.androhit.natively.camera.data.CameraController

class CameraViewModel(
    private val cameraController: CameraController,
): ViewModel() {

    fun switchCamera() {
        cameraController.switchCamera()
    }

    fun attachTextAnalyzer(analyzer: MlKitAnalyzer) {
        cameraController.setAnalyzer(analyzer)
    }

    fun capturePhoto() {
        /** TODO("Capture photo and store it") **/
    }
}