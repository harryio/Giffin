package io.github.sainiharry.giffin.gif

import io.github.sainiharry.giffin.common.Gif
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    suspend fun fetchTrendingGifs(): List<Gif>
}

internal class GifRepositoryImpl(
    private val gifService: GifService,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : GifRepository {

    override suspend fun fetchTrendingGifs(): List<Gif> = withContext(coroutineDispatcher) {
        val trendingGifsResponse = gifService.fetchTrendingGifs()
        val gifResponses = trendingGifsResponse?.data ?: emptyList()
        gifResponses.mapNotNull {
            it.toGif()
        }
    }
}