package io.github.sainiharry.giffin.gif

import androidx.paging.Pager
import androidx.paging.PagingConfig
import io.github.sainiharry.giffin.common.Gif
import org.koin.dsl.module
import retrofit2.Retrofit

val gifRepositoryModule = module {
    single<GifService> {
        get<Retrofit>().create(GifService::class.java)
    }

    single<GifRepository> {
        GifRepositoryImpl(get())
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