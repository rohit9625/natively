package dev.androhit.natively.camera.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController.IMAGE_ANALYSIS
import androidx.camera.view.CameraController.IMAGE_CAPTURE
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner

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

    fun clearAnalyzer() {
        controller.clearImageAnalysisAnalyzer()
    }

    fun capturePhoto(
        onResult: (Result<Bitmap>) -> Unit
    ) {
        controller.takePicture(
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    val matrix = Matrix().apply {
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                    }
                    val rotatedBitmap = Bitmap.createBitmap(
                        image.toBitmap(),
                        0,
                        0,
                        image.width,
                        image.height,
                        matrix,
                        true
                    )
                    onResult(Result.success(rotatedBitmap))
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
