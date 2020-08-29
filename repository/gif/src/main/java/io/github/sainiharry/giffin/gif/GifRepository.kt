package io.github.sainiharry.giffin.gif

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import io.github.sainiharry.giffin.common.Gif
import io.github.sainiharry.giffin.commonrepository.DATABASE_NAME
import io.github.sainiharry.giffin.gif.database.GifDatabase
import io.github.sainiharry.giffin.gif.network.GifService
import org.koin.dsl.module
import retrofit2.Retrofit

val gifRepositoryModule = module {
    single<GifService> {
        get<Retrofit>().create(GifService::class.java)
    }

    single<GifRepository> {
        GifRepositoryImpl(get())
    }

    single {
        Room.databaseBuilder(get(), GifDatabase::class.java, DATABASE_NAME).build()
    }
}

interface GifRepository {

    fun getTrendingGifsPager(): Pager<Int, Gif>
}

internal class GifRepositoryImpl(private val gifService: GifService) : GifRepository {

    override fun getTrendingGifsPager(): Pager<Int, Gif> {
        return Pager(PagingConfig(pageSize = 20, enablePlaceholders = false)) {
            TrendingGifsDataSource(gifService)
        }
    }
}