package io.github.sainiharry.giffin

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import io.github.sainiharry.giffin.gif.gifRepositoryModule
import io.github.sainiharry.giffin.network.API_KEY_QUALIFIER
import io.github.sainiharry.giffin.network.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val SHARED_PREF_NAME = "GIFFIN_SHARED_PREFERENCES"

class GiffinApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@GiffinApp)

            val apiKeyModule = module {
                single(named(API_KEY_QUALIFIER)) {
                    BuildConfig.GIPHY_API_KEY
                }
            }

            val sharedPreferencesModule = module {
                single<SharedPreferences> {
                    get<Context>().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                }
            }

            modules(apiKeyModule, sharedPreferencesModule, networkModule, gifRepositoryModule)
        }
    }
}