package dev.androhit.natively.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.androhit.natively.camera.data.CameraController
import dev.androhit.natively.camera.ui.CameraScreen
import dev.androhit.natively.camera.ui.ScriptSelectionScreen
import dev.androhit.natively.camera.ui.scripts
import org.koin.compose.koinInject

@Composable
fun MainNavigation(modifier: Modifier = Modifier) {
    val mainBackStack = rememberNavBackStack(Route.SelectScript)

    NavDisplay(
        backStack = mainBackStack,
        onBack = { mainBackStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Route.Camera> {
                val cameraController = koinInject<CameraController>()

                CameraScreen(
                    cameraController = cameraController,
                    script = it.script
                )
            }

            entry<Route.SelectScript> {
                ScriptSelectionScreen {
                    mainBackStack.add(Route.Camera(it))
                }
            }
        }
    )
}