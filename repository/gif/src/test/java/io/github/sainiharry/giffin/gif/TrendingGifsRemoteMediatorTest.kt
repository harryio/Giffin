package io.github.sainiharry.giffin.gif

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.RemoteMediator
import io.github.sainiharry.giffin.gif.database.GifDao
import io.github.sainiharry.giffin.gif.network.GifResponse
import io.github.sainiharry.giffin.gif.network.GifResponseWrapper
import io.github.sainiharry.giffin.gif.network.GifService
import io.github.sainiharry.giffin.gif.network.toGifEntity
import io.github.sainiharry.giffin.gif.paging.GifPagingKeyStore
import io.github.sainiharry.giffin.gif.paging.TrendingGifsRemoteMediator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class TrendingGifsRemoteMediatorTest {

    @Mock
    private lateinit var gifService: GifService

    @Mock
    private lateinit var gifDao: GifDao

    @Mock
    private lateinit var pagingKeyStore: GifPagingKeyStore

    private lateinit var mediator: TrendingGifsRemoteMediator

    private val pagingState = getMockPagingState()

    @Before
    fun setup() {
        mediator = TrendingGifsRemoteMediator(gifService, gifDao, pagingKeyStore)
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
        `when`(
            gifService.fetchTrendingGifs(
                anyInt(),
                anyInt()
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
        `when`(gifService.fetchTrendingGifs(anyInt(), anyInt())).thenReturn(gifResponseWrapper)

        var result = mediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        result = mediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun testPagination() = runBlockingTest {
        `when`(gifService.fetchTrendingGifs(anyInt(), anyInt())).thenReturn(
            getMockGifResponseWrapper()
        )

        val result = mediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun testRefresh() = runBlockingTest {
        val mockGifResponse = getMockGifResponseWrapper()
        `when`(gifService.fetchTrendingGifs(eq(0), anyInt())).thenReturn(
            getMockGifResponseWrapper()
        )

        val result = mediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        val gifResponses = mockGifResponse.data!!
        val pagination = mockGifResponse.pagination
        val gifList = gifResponses.mapNotNull {
            it.toGifEntity()
        }

        verify(gifService).fetchTrendingGifs(0, 20)
        verify(gifDao).insert(gifList, true)
        verify(pagingKeyStore).setKey(pagination!!.count + pagination.offset)
        verifyNoMoreInteractions(gifDao)
        verifyNoMoreInteractions(pagingKeyStore)
        verifyNoMoreInteractions(gifService)
    }

    @Test
    fun testAppend() = runBlockingTest {
        val offset = 120
        `when`(pagingKeyStore.getKey()).thenReturn(offset)
        val mockGifResponse = getMockGifResponseWrapper()
        `when`(gifService.fetchTrendingGifs(eq(offset), anyInt())).thenReturn(
            getMockGifResponseWrapper()
        )

        val result = mediator.load(LoadType.APPEND, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        val gifResponses = mockGifResponse.data!!
        val pagination = mockGifResponse.pagination
        val gifList = gifResponses.mapNotNull {
            it.toGifEntity()
        }

        verify(gifService).fetchTrendingGifs(120, 20)
        verify(gifDao).insert(gifList, false)
        verify(pagingKeyStore).getKey()
        verify(pagingKeyStore).setKey(pagination!!.count + pagination.offset)
        verifyNoMoreInteractions(gifDao)
        verifyNoMoreInteractions(pagingKeyStore)
        verifyNoMoreInteractions(gifService)
    }

    @Test
    fun testPaginationEndWithNullGifImage() = runBlockingTest {
        val mockGifResponse = GifResponseWrapper(
            listOf(GifResponse("id", getMockImageWrapperWithNullImage())),
            getMockPagination()
        )
        `when`(gifService.fetchTrendingGifs(eq(0), anyInt())).thenReturn(mockGifResponse)

        val result = mediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        val gifResponses = mockGifResponse.data!!
        val pagination = mockGifResponse.pagination
        val gifList = gifResponses.mapNotNull {
            it.toGifEntity()
        }

        assertTrue(gifList.isEmpty())
        verify(gifService).fetchTrendingGifs(0, 20)
        verify(gifDao).insert(gifList, true)
        verify(pagingKeyStore).setKey(pagination!!.count + pagination.offset)
        verifyNoMoreInteractions(gifDao)
        verifyNoMoreInteractions(pagingKeyStore)
        verifyNoMoreInteractions(gifService)
    }

    @Test
    fun testPaginationWithEmptyGifResult() = runBlockingTest {
        val mockGifResponse = GifResponseWrapper(
            emptyList(),
            getMockPagination()
        )
        `when`(gifService.fetchTrendingGifs(eq(0), anyInt())).thenReturn(mockGifResponse)

        val result = mediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        val gifResponses = mockGifResponse.data!!
        val pagination = mockGifResponse.pagination
        val gifList = gifResponses.mapNotNull {
            it.toGifEntity()
        }

        assertTrue(gifList.isEmpty())
        verify(gifService).fetchTrendingGifs(0, 20)
        verify(gifDao).insert(gifList, true)
        verify(pagingKeyStore).setKey(pagination!!.count + pagination.offset)
        verifyNoMoreInteractions(gifDao)
        verifyNoMoreInteractions(pagingKeyStore)
        verifyNoMoreInteractions(gifService)
    }
}