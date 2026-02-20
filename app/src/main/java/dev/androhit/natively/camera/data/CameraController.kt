package dev.androhit.natively.camera.data

import android.content.Context
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController.IMAGE_ANALYSIS
import androidx.camera.view.CameraController.IMAGE_CAPTURE
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.io.File

class CameraController(
    private val context: Context
) {

    val controller = LifecycleCameraController(context).apply {
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        imageCaptureFlashMode = ImageCapture.FLASH_MODE_OFF
        setEnabledUseCases(IMAGE_ANALYSIS or IMAGE_CAPTURE)
    }


    fun bindToLifecycle(owner: LifecycleOwner) {
        controller.bindToLifecycle(owner)
    }

    fun switchCamera() {
        controller.cameraSelector =
            if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                CameraSelector.DEFAULT_FRONT_CAMERA
            else
                CameraSelector.DEFAULT_BACK_CAMERA
    }

    fun setAnalyzer(analyzer: MlKitAnalyzer) {
        controller.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(context),
            analyzer
        )
    }

    fun capturePhoto(
        outputFile: File,
        onResult: (Result<Uri>) -> Unit
    ) {
        val options = ImageCapture.OutputFileOptions.Builder(outputFile).build()

        controller.takePicture(
            options,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(result: ImageCapture.OutputFileResults) {
                    onResult(Result.success(result.savedUri!!))
                }

                override fun onError(exception: ImageCaptureException) {
                    onResult(Result.failure(exception))
                }
            }
        )
    }

    fun detach() {
        controller.unbind()
    }
}
