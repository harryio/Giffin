package io.github.sainiharry.giffin

import android.app.Application
import io.github.sainiharry.giffin.network.networkModule
import org.koin.core.context.startKoin

class GiffinApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(networkModule)
        }
    }
}