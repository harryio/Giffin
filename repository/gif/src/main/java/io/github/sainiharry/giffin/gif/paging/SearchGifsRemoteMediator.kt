package io.github.sainiharry.giffin.gif.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import io.github.sainiharry.giffin.common.Gif
import io.github.sainiharry.giffin.gif.database.SearchDao
import io.github.sainiharry.giffin.gif.network.GifService
import io.github.sainiharry.giffin.gif.network.toSearchedGif

private const val PAGE_SIZE = 20

@OptIn(ExperimentalPagingApi::class)
internal class SearchGifsRemoteMediator(
    private val gifService: GifService,
    private val searchDao: SearchDao,
    private val query: String?
) : RemoteMediator<Int, Gif>() {

    private var key: Int = 0

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Gif>): MediatorResult {
        return try {
            val offset = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> key
            }

            if (query.isNullOrEmpty() || query.trim().isBlank()) {
                searchDao.clearAll()
                key = 0
                MediatorResult.Success(endOfPaginationReached = true)
            } else {
                val searchedGifResponse = gifService.searchGifs(query.trim(), offset, PAGE_SIZE)
                val gifResponses = searchedGifResponse?.data ?: emptyList()
                val gifList = gifResponses.mapNotNull {
                    it.toSearchedGif()
                }

                searchedGifResponse?.pagination?.let {
                    searchDao.insert(gifList, refresh = loadType == LoadType.REFRESH)
                    key = it.count + it.offset
                    MediatorResult.Success(endOfPaginationReached = gifResponses.isEmpty())
                } ?: MediatorResult.Success(endOfPaginationReached = true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            MediatorResult.Error(e)
        }
    }
}