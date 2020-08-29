package io.github.sainiharry.giffin.gif.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import io.github.sainiharry.giffin.common.Gif
import io.github.sainiharry.giffin.gif.database.GifDao
import io.github.sainiharry.giffin.gif.network.GifService
import io.github.sainiharry.giffin.gif.network.toGifEntity

private const val PAGE_SIZE = 20

@OptIn(ExperimentalPagingApi::class)
internal class TrendingGifsRemoteMediator(
    private val gifService: GifService,
    private val gifDao: GifDao,
    private val pagingKeyStore: GifPagingKeyStore
) : RemoteMediator<Int, Gif>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Gif>
    ): MediatorResult {
        return try {
            val offset = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val key = pagingKeyStore.getKey()
                    key ?: return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }
            }

            val trendingGifsResponse = gifService.fetchTrendingGifs(offset, PAGE_SIZE)
            val gifResponses = trendingGifsResponse?.data ?: emptyList()
            val gifList = gifResponses.mapNotNull {
                it.toGifEntity()
            }

            return trendingGifsResponse?.pagination?.let {
                gifDao.insert(gifList, refresh = loadType == LoadType.REFRESH)
                pagingKeyStore.setKey(it.count + it.offset)
                MediatorResult.Success(endOfPaginationReached = gifList.isEmpty())
            } ?: MediatorResult.Success(endOfPaginationReached = true)
        } catch (e: Exception) {
            e.printStackTrace()
            MediatorResult.Error(e)
        }
    }
}