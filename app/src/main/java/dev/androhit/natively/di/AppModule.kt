package dev.androhit.natively.di

import dev.androhit.natively.camera.data.CameraController
import dev.androhit.natively.camera.ui.CameraViewModel
import dev.androhit.natively.data.TextAnalyzer
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::TextAnalyzer)
    singleOf(::CameraController)
    viewModelOf(::CameraViewModel)
}