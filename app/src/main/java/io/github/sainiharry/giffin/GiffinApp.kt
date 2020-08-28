package io.github.sainiharry.giffin

import android.app.Application
import io.github.sainiharry.giffin.network.API_KEY_QUALIFIER
import io.github.sainiharry.giffin.network.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

class GiffinApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@GiffinApp)

            module {
                single(named(API_KEY_QUALIFIER)) {
                    BuildConfig.GIPHY_API_KEY
                }
            }

            modules(networkModule)
        }
    }
}