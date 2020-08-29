package io.github.sainiharry.giffin.gif

import android.content.Context
import androidx.room.Room
import io.github.sainiharry.giffin.commonrepository.DATABASE_NAME
import io.github.sainiharry.giffin.gif.database.GifDatabase
import io.github.sainiharry.giffin.gif.network.GifService
import io.github.sainiharry.giffin.gif.paging.GifPagingKeyStore
import io.github.sainiharry.giffin.gif.paging.SharedPrefPagingKeyStore
import org.koin.dsl.module
import retrofit2.Retrofit

val gifRepositoryModule = module {
    single<GifService> {
        get<Retrofit>().create(GifService::class.java)
    }

    single {
        Room.databaseBuilder(get(), GifDatabase::class.java, DATABASE_NAME).build()
    }

    single<GifPagingKeyStore> {
        SharedPrefPagingKeyStore(get())
    }

    single<GifRepository> {
        val context = get<Context>()
        GifRepositoryImpl(get(), get(), get())
    }
}