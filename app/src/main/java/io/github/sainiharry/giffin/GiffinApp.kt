package io.github.sainiharry.giffin

import android.app.Application
import io.github.sainiharry.giffin.network.networkModule
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

class GiffinApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            module {
                single(named("api_key")) {
                    BuildConfig.GIPHY_API_KEY
                }
            }
            
            modules(networkModule)
        }
    }
}