package dev.androhit.natively.camera.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.androhit.natively.R
import dev.androhit.natively.camera.data.CameraController
import dev.androhit.natively.camera.ui.components.CameraPreview

@Composable
fun CameraScreen(
    cameraController: CameraController,
) {
    val viewModel = viewModel {
        CameraViewModel(cameraController)
    }

    DisposableEffect(Unit) {
        onDispose {
            cameraController.detach()
        }
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            CameraPreview(
                controller = cameraController,
                onReady = {  },
                modifier = Modifier.fillMaxSize()
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                ) {
                    IconButton(
                        onClick = { viewModel.capturePhoto() },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_camera),
                            contentDescription = "Take picture",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }
        }
    }
}
