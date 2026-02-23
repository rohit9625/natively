package dev.androhit.natively.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.androhit.natively.camera.data.CameraController
import dev.androhit.natively.camera.ui.CameraScreen
import dev.androhit.natively.camera.ui.CameraViewModel
import dev.androhit.natively.camera.ui.ScriptSelectionScreen
import dev.androhit.natively.domain.TextScript
import dev.androhit.natively.ui.screens.ViewImageScreen
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainNavigation(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<CameraViewModel>()
    val userPres by viewModel.userPreferences.collectAsStateWithLifecycle()

    val mainBackStack = rememberNavBackStack(
        if(userPres?.isFirstLaunch ?: false) Route.SelectScript
        else Route.Camera
    )

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
                    viewModel = viewModel,
                    onViewImage = {
                        mainBackStack.add(Route.ViewImage)
                    },
                    onChangeScript = {
                        mainBackStack.add(Route.SelectScript)
                    }
                )
            }

            entry<Route.ViewImage> {
                ViewImageScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        mainBackStack.removeLastOrNull()
                    }
                )
            }

            entry<Route.SelectScript> {
                ScriptSelectionScreen(
                    isFirstLaunch = userPres?.isFirstLaunch ?: true,
                    script = userPres?.preferredScript ?: TextScript.Latin,
                    onProceed = {
                        viewModel.updatePreferredScript(it)
                        viewModel.updateIsFirstLaunch(false)
                        mainBackStack.add(Route.Camera)
                    }
                )
            }
        }
    )
}