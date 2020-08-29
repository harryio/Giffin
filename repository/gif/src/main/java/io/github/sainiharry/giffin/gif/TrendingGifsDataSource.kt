package io.github.sainiharry.giffin.gif

import androidx.paging.PagingSource
import io.github.sainiharry.giffin.common.Gif
import io.github.sainiharry.giffin.gif.network.GifService

//internal class TrendingGifsDataSource(private val gifService: GifService) :
//    PagingSource<Int, Gif>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gif> {
//        return try {
//            val nextPageOffset = params.key ?: 0
//            val trendingGifsResponse = gifService.fetchTrendingGifs(nextPageOffset, 20)
//            val gifResponses = trendingGifsResponse?.data ?: emptyList()
//            val gifList = gifResponses.mapNotNull {
//                it.toGif()
//            }
//
//            val newOffset = trendingGifsResponse?.pagination?.let {
//                it.count + it.offset
//            }
//
//            LoadResult.Page(gifList, null, newOffset)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            LoadResult.Error(e)
//        }
//    }
//}