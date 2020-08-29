package io.github.sainiharry.giffin.gif

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.github.sainiharry.giffin.common.Gif
import io.github.sainiharry.giffin.gif.database.GifDao
import io.github.sainiharry.giffin.gif.network.GifService
import kotlinx.coroutines.flow.Flow

interface GifRepository {

    fun getTrendingGifsPager(): Flow<PagingData<Gif>>
}

internal class GifRepositoryImpl(
    private val gifService: GifService,
    private val gifDao: GifDao,
    private val keyStore: GifPagingKeyStore
) : GifRepository {


    override fun getTrendingGifsPager(): Flow<PagingData<Gif>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = TrendingGifsRemoteMediator(gifService, gifDao, keyStore)
        ) {
            gifDao.pagingSource()
        }.flow
}