package dev.androhit.natively

import android.app.Application
import dev.androhit.natively.di.cameraModule
import dev.androhit.natively.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class NativelyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@NativelyApplication)
            modules(networkModule, cameraModule)
        }
    }
}