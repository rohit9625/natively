package dev.androhit.natively.camera.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.androhit.natively.R
import dev.androhit.natively.camera.data.CameraController
import dev.androhit.natively.camera.ui.components.CameraPreview
import dev.androhit.natively.data.TextAnalyzer
import dev.androhit.natively.domain.RecognizedText
import dev.androhit.natively.ui.components.TranslateTextChip
import kotlin.collections.forEach

@Composable
fun CameraScreen(
    cameraController: CameraController,
) {
    val context = LocalContext.current.applicationContext
    val viewModel = viewModel {
        CameraViewModel(cameraController)
    }
    val textAnalyzer = remember {
        TextAnalyzer(context, viewModel::onTextDetected)
    }

    LaunchedEffect(Unit) {
        viewModel.attachTextAnalyzer(textAnalyzer.getInstance())
    }

    DisposableEffect(Unit) {
        onDispose {
            cameraController.detach()
            textAnalyzer.close()
        }
    }

    val detectedTextLines by viewModel.detectedTextLines.collectAsStateWithLifecycle()
    var selectedTextLine by remember { mutableStateOf<RecognizedText?>(null) }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            CameraPreview(
                controller = cameraController,
                onReady = {  },
                modifier = Modifier.fillMaxSize()
            )

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures { tapOffset ->
                            val tapped = detectedTextLines.lastOrNull { line ->
                                line.boundingBox.contains(tapOffset.x.toInt(), tapOffset.y.toInt())
                            }
                            selectedTextLine = tapped
                        }
                    }
            ) {
                detectedTextLines.forEach { line ->
                    val rect = line.boundingBox

                    drawRoundRect(
                        color = Color(0xFF00E5FF).copy(alpha = 0.18f),
                        topLeft = Offset(rect.left.toFloat(), rect.top.toFloat()),
                        size = Size(rect.width().toFloat(), rect.height().toFloat()),
                        cornerRadius = CornerRadius(8.dp.toPx())
                    )

                    drawRoundRect(
                        color = Color.White.copy(alpha = 0.8f),
                        topLeft = Offset(rect.left.toFloat(), rect.top.toFloat()),
                        size = Size(rect.width().toFloat(), rect.height().toFloat()),
                        cornerRadius = CornerRadius(8.dp.toPx()),
                        style = Stroke(width = 1.dp.toPx())
                    )
                }
            }

            selectedTextLine?.let { line ->
                DetectedTextChipsLayer(
                    textLines = listOf(line),
                    onTranslate = {
                        /** TODO("Translate text") **/
                    }
                )
            }

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

@Composable
fun DetectedTextChipsLayer(
    textLines: List<RecognizedText>,
    onTranslate: (RecognizedText) -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val maxWidthPx = constraints.maxWidth
        val maxHeightPx = constraints.maxHeight

        textLines.forEach { line ->
            val rect = line.boundingBox
            var chipSize by remember { mutableStateOf(IntSize.Zero) }

            TranslateTextChip(
                text = line.text,
                onTranslate = { onTranslate(line) },
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        chipSize = coordinates.size
                    }
                    .offset {
                        val x = rect.right.coerceIn(0, maxWidthPx - chipSize.width)
                        val y = (rect.bottom - 72).coerceIn(0, maxHeightPx - chipSize.height)
                        IntOffset(x, y)
                    }
                    .widthIn(max = 240.dp)
            )
        }
    }
}
