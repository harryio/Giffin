package io.github.sainiharry.giffin.gif

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.RemoteMediator
import io.github.sainiharry.giffin.gif.database.SearchDao
import io.github.sainiharry.giffin.gif.network.GifResponse
import io.github.sainiharry.giffin.gif.network.GifResponseWrapper
import io.github.sainiharry.giffin.gif.network.GifService
import io.github.sainiharry.giffin.gif.network.toSearchedGif
import io.github.sainiharry.giffin.gif.paging.SearchGifsRemoteMediator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class SearchGifsRemoteMediatorTest {

    @Mock
    private lateinit var gifService: GifService

    @Mock
    private lateinit var searchDao: SearchDao

    private lateinit var mediator: SearchGifsRemoteMediator

    private val pagingState = getMockPagingState()

    private var query = "hello"

    @Before
    fun setup() {
        mediator = SearchGifsRemoteMediator(gifService, searchDao, query)
    }

    @Test
    fun testPrepend() = runBlockingTest {
        var result = mediator.load(LoadType.PREPEND, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        result = mediator.load(LoadType.PREPEND, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        result = mediator.load(LoadType.PREPEND, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun testFetchError() = runBlockingTest {
        Mockito.`when`(
            gifService.searchGifs(
                eq(query),
                Mockito.anyInt(),
                Mockito.anyInt()
            )
        ).thenThrow(IllegalArgumentException())

        var result = mediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Error)

        result = mediator.load(LoadType.APPEND, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    @Test
    fun testPaginationWithNullPaginationData() = runBlockingTest {
        val gifResponseWrapper = GifResponseWrapper(listOf(getMockGifResponse()), null)
        Mockito.`when`(gifService.searchGifs(eq(query), Mockito.anyInt(), Mockito.anyInt()))
            .thenReturn(gifResponseWrapper)

        var result = mediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        result = mediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun testPagination() = runBlockingTest {
        Mockito.`when`(gifService.searchGifs(eq(query), Mockito.anyInt(), Mockito.anyInt())).thenReturn(
            getMockGifResponseWrapper()
        )

        val result = mediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun testRefresh() = runBlockingTest {
        val mockGifResponse = getMockGifResponseWrapper()
        Mockito.`when`(gifService.searchGifs(eq(query), ArgumentMatchers.eq(0), Mockito.anyInt()))
            .thenReturn(
                getMockGifResponseWrapper()
            )

        val result = mediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        val gifResponses = mockGifResponse.data!!
        val gifList = gifResponses.mapNotNull {
            it.toSearchedGif()
        }

        Mockito.verify(gifService).searchGifs(query, 0, 20)
        Mockito.verify(searchDao).insert(gifList, true)
        Mockito.verifyNoMoreInteractions(searchDao)
        Mockito.verifyNoMoreInteractions(gifService)
    }

    @Test
    fun testAppend() = runBlockingTest {
        var offset = 0
        val mockGifResponse = getMockGifResponseWrapper()
        Mockito.`when`(gifService.searchGifs(eq(query), ArgumentMatchers.eq(offset), Mockito.anyInt()))
            .thenReturn(
                getMockGifResponseWrapper()
            )

        val firstPageResult = mediator.load(LoadType.APPEND, pagingState)
        assertTrue(firstPageResult is RemoteMediator.MediatorResult.Success)
        assertFalse((firstPageResult as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        val firstPageGifResponses = mockGifResponse.data!!
        val firstPageGifList = firstPageGifResponses.mapNotNull {
            it.toSearchedGif()
        }

        Mockito.verify(gifService).searchGifs(query, offset, 20)
        Mockito.verify(searchDao).insert(firstPageGifList, false)

        offset = mockGifResponse.pagination!!.count + mockGifResponse.pagination.offset
        Mockito.`when`(gifService.searchGifs(eq(query), ArgumentMatchers.eq(offset), Mockito.anyInt()))
            .thenReturn(
                getMockGifResponseWrapper()
            )

        val secondPageResult = mediator.load(LoadType.APPEND, pagingState)
        assertTrue(secondPageResult is RemoteMediator.MediatorResult.Success)
        assertFalse((secondPageResult as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        val secondPageGifResponses = mockGifResponse.data
        val secondPageGifList = secondPageGifResponses.mapNotNull {
            it.toSearchedGif()
        }

        Mockito.verify(gifService).searchGifs(query, offset, 20)
        Mockito.verify(searchDao, times(2)).insert(secondPageGifList, false)

        Mockito.verifyNoMoreInteractions(searchDao)
        Mockito.verifyNoMoreInteractions(gifService)
    }

    @Test
    fun testPaginationEndWithNullGifImage() = runBlockingTest {
        val mockGifResponse = GifResponseWrapper(
            listOf(GifResponse("id", getMockImageWrapperWithNullImage())),
            getMockPagination()
        )
        Mockito.`when`(gifService.searchGifs(eq(query), ArgumentMatchers.eq(0), Mockito.anyInt()))
            .thenReturn(mockGifResponse)

        val result = mediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        val gifResponses = mockGifResponse.data!!
        val gifList = gifResponses.mapNotNull {
            it.toSearchedGif()
        }

        assertTrue(gifList.isEmpty())
        Mockito.verify(gifService).searchGifs(query, 0, 20)
        Mockito.verify(searchDao).insert(gifList, true)
        Mockito.verifyNoMoreInteractions(searchDao)
        Mockito.verifyNoMoreInteractions(gifService)
    }

    @Test
    fun testPaginationWithEmptyGifResult() = runBlockingTest {
        val mockGifResponse = GifResponseWrapper(
            emptyList(),
            getMockPagination()
        )
        Mockito.`when`(gifService.searchGifs(eq(query), ArgumentMatchers.eq(0), Mockito.anyInt()))
            .thenReturn(mockGifResponse)

        val result = mediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        val gifResponses = mockGifResponse.data!!
        val gifList = gifResponses.mapNotNull {
            it.toSearchedGif()
        }

        assertTrue(gifList.isEmpty())
        Mockito.verify(gifService).searchGifs(query, 0, 20)
        Mockito.verify(searchDao).insert(gifList, true)
        Mockito.verifyNoMoreInteractions(searchDao)
        Mockito.verifyNoMoreInteractions(gifService)
    }

    @Test
    fun testRefreshWithInvalidQuery() = runBlockingTest {
        var mediator = SearchGifsRemoteMediator(gifService, searchDao, "")
        var result = mediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        mediator = SearchGifsRemoteMediator(gifService, searchDao, null)
        result = mediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        mediator = SearchGifsRemoteMediator(gifService, searchDao, "      ")
        result = mediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun testAppendWithInvalidQuery() = runBlockingTest {
        var mediator = SearchGifsRemoteMediator(gifService, searchDao, "")
        var result = mediator.load(LoadType.APPEND, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        mediator = SearchGifsRemoteMediator(gifService, searchDao, null)
        result = mediator.load(LoadType.APPEND, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        mediator = SearchGifsRemoteMediator(gifService, searchDao, "      ")
        result = mediator.load(LoadType.APPEND, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }
}