package dev.androhit.natively.camera.ui.components

import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import dev.androhit.natively.camera.data.CameraController

@Composable
fun CameraPreview(
    controller: CameraController,
    onReady: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        factory = {
            PreviewView(it).apply {
                this.controller = controller.controller
                controller.bindToLifecycle(lifecycleOwner)
                onReady()
            }
        },
        modifier = modifier,
    )
}
