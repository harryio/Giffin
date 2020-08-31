package io.github.sainiharry.giffin.gif

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.github.sainiharry.giffin.common.Gif
import io.github.sainiharry.giffin.commonrepository.PaginationResponse
import io.github.sainiharry.giffin.gif.network.GifImage
import io.github.sainiharry.giffin.gif.network.GifResponse
import io.github.sainiharry.giffin.gif.network.GifResponseWrapper
import io.github.sainiharry.giffin.gif.network.ImagesWrapper
import org.mockito.Mockito

fun <T> eq(obj: T): T = Mockito.eq(obj)

fun getMockPagingState() = PagingState(getMockGifPageList(), 0, getMockPageConfig(), 10)

fun getMockPageConfig() = PagingConfig(20)

fun getMockGifPageList() = listOf(getMockGifPage())

fun getMockGifPage() = PagingSource.LoadResult.Page(getMockGifList(), 0, 1)

fun getMockGifList() = listOf(getMockGif())

fun getMockGif() = Gif("1", "https://gifurl.com", 360, 180, false)

internal fun getMockGifResponseWrapper() = GifResponseWrapper(listOf(getMockGifResponse()), getMockPagination())

internal fun getMockGifResponse() = GifResponse("883", getMockImagesWrapper())

internal fun getMockImageWrapperWithNullImage() = ImagesWrapper(getMockGifImageWithNullUrl())

internal fun getMockImagesWrapper() = ImagesWrapper(getMockGifImage())

internal fun getMockGifImageWithNullUrl() = GifImage(null)

internal fun getMockGifImage() = GifImage("http://gifurl.com", 120, 120)

fun getMockPagination() = PaginationResponse(100, 20, 0)