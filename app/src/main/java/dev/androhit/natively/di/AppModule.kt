package dev.androhit.natively.di

import android.content.Context
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import dev.androhit.natively.camera.data.CameraController
import dev.androhit.natively.camera.ui.CameraViewModel
import dev.androhit.natively.data.TextAnalyzer
import dev.androhit.natively.data.UserPrefRepositoryImpl
import dev.androhit.natively.domain.UserPrefRepository
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

private const val USER_PREFERENCES_NAME = "user.preferences_pb"

val appModule = module {
    singleOf(::UserPrefRepositoryImpl) bind UserPrefRepository::class
    single {
        PreferenceDataStoreFactory.create(
            produceFile = { get<Context>().dataStoreFile(USER_PREFERENCES_NAME) }
        )
    }
    singleOf(::TextAnalyzer)
    singleOf(::CameraController)
    viewModelOf(::CameraViewModel)
}