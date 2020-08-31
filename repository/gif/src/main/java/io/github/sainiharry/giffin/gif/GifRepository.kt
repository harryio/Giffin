package io.github.sainiharry.giffin.gif

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.github.sainiharry.giffin.common.Gif
import io.github.sainiharry.giffin.gif.database.FavoriteGifEntity
import io.github.sainiharry.giffin.gif.database.GifDatabase
import io.github.sainiharry.giffin.gif.database.toGifEntity
import io.github.sainiharry.giffin.gif.network.GifService
import io.github.sainiharry.giffin.gif.paging.GifPagingKeyStore
import io.github.sainiharry.giffin.gif.paging.SearchGifsRemoteMediator
import io.github.sainiharry.giffin.gif.paging.TrendingGifsRemoteMediator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Repository for accessing Gif data
 */
interface GifRepository {

    /**
     * Get trending gifs
     * @return a [Flow] that provides trending gifs pages
     */
    fun getTrendingGifsPager(): Flow<PagingData<Gif>>

    /**
     * Get favorite gifs
     * @return a [Flow] that provides latest list of favorite gifs
     */
    fun getFavoriteGifs(): Flow<List<Gif>>

    /**
     * Search Gifs
     * @param query the query term or phrase for which the gifs are to be fetched
     * @return a [Flow] that provides pages of gifs per the search term
     */
    fun searchGifs(query: String?): Flow<PagingData<Gif>>

    /**
     * Favorite a gif
     * @param gif the gif which is to be favorited
     */
    suspend fun favoriteGif(gif: Gif)

    /**
     * UnFavorite a gif
     * @param gif the gif which is to be unfavorited
     */
    suspend fun unFavoriteGif(gif: Gif)

    /**
     * Clear gifs from the last searched term
     */
    suspend fun clearSearchResults()
}

/**
 * Internal implementation of [GifRepository]
 */
internal class GifRepositoryImpl(
    private val gifService: GifService,
    gifDatabase: GifDatabase,
    private val keyStore: GifPagingKeyStore,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : GifRepository {

    private val gifDao = gifDatabase.gifDao()

    private val favoriteGifsDao = gifDatabase.favoriteGifsDao()

    private val searchDao = gifDatabase.seachedGifsDao()

    override fun getTrendingGifsPager(): Flow<PagingData<Gif>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = TrendingGifsRemoteMediator(gifService, gifDao, keyStore)
        ) {
            gifDao.pagingSource()
        }.flow

    override fun getFavoriteGifs(): Flow<List<Gif>> = favoriteGifsDao.getFavoriteGifs()

    override fun searchGifs(query: String?): Flow<PagingData<Gif>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = SearchGifsRemoteMediator(gifService, searchDao, query)
        ) {
            searchDao.pagingSource()
        }.flow

    override suspend fun favoriteGif(gif: Gif) = withContext(coroutineDispatcher) {
        favoriteGifsDao.insert(FavoriteGifEntity(gif.toGifEntity()))
    }

    override suspend fun unFavoriteGif(gif: Gif) = withContext(coroutineDispatcher) {
        favoriteGifsDao.remove(gif.id)
    }

    override suspend fun clearSearchResults() {
        searchDao.clearAll()
    }
}