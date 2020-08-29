package io.github.sainiharry.giffin.gif

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.github.sainiharry.giffin.common.Gif
import io.github.sainiharry.giffin.gif.database.GifDatabase
import io.github.sainiharry.giffin.gif.network.GifService
import io.github.sainiharry.giffin.gif.paging.GifPagingKeyStore
import io.github.sainiharry.giffin.gif.paging.TrendingGifsRemoteMediator
import kotlinx.coroutines.flow.Flow

interface GifRepository {

    fun getTrendingGifsPager(): Flow<PagingData<Gif>>

    fun getFavoriteGifs(): Flow<List<Gif>>
}

internal class GifRepositoryImpl(
    private val gifService: GifService,
    gifDatabase: GifDatabase,
    private val keyStore: GifPagingKeyStore
) : GifRepository {

    private val gifDao = gifDatabase.gifDao()

    private val favoriteGifsDao = gifDatabase.favoriteGifsDao()

    override fun getTrendingGifsPager(): Flow<PagingData<Gif>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = TrendingGifsRemoteMediator(gifService, gifDao, keyStore)
        ) {
            gifDao.pagingSource()
        }.flow

    override fun getFavoriteGifs(): Flow<List<Gif>> = favoriteGifsDao.getFavoriteGifs()
}