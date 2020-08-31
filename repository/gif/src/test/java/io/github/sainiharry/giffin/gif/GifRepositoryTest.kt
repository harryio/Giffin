package io.github.sainiharry.giffin.gif

import io.github.sainiharry.giffin.gif.database.*
import io.github.sainiharry.giffin.gif.network.GifService
import io.github.sainiharry.giffin.gif.paging.GifPagingKeyStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GifRepositoryTest {

    private lateinit var gifDao: GifDao

    @Mock
    private lateinit var favoriteGifDao: FavoriteGifsDao

    @Mock
    private lateinit var searchDao: SearchDao

    private lateinit var gifRepository: GifRepository

    @Before
    fun setup() {
        val gifService = Mockito.mock(GifService::class.java)
        val gifDatabase = Mockito.mock(GifDatabase::class.java)
        val keyStore = Mockito.mock(GifPagingKeyStore::class.java)

        gifDao = Mockito.mock(GifDao::class.java)
        `when`(gifDatabase.gifDao()).thenReturn(gifDao)
        `when`(gifDatabase.favoriteGifsDao()).thenReturn(favoriteGifDao)
        `when`(gifDatabase.seachedGifsDao()).thenReturn(searchDao)

        gifRepository =
            GifRepositoryImpl(gifService, gifDatabase, keyStore, TestCoroutineDispatcher())
    }

    @Test
    fun testGetFavoriteGifs() {
        gifRepository.getFavoriteGifs()
        verify(favoriteGifDao).getFavoriteGifs()
        verifyNoMoreInteractions(favoriteGifDao)
    }

    @Test
    fun testFavoriteGif() = runBlockingTest {
        val gif = getMockGif()
        gifRepository.favoriteGif(gif)
        verify(favoriteGifDao).insert(FavoriteGifEntity(gif.toGifEntity()))
        verifyNoMoreInteractions(favoriteGifDao)
    }

    @Test
    fun testUnFavoriteGif() = runBlockingTest {
        val gif = getMockGif()
        gifRepository.unFavoriteGif(gif)
        verify(favoriteGifDao).remove(gif.id)
        verifyNoMoreInteractions(favoriteGifDao)
    }

    @Test
    fun testClearSearchResults() = runBlockingTest {
        gifRepository.clearSearchResults()
        verify(searchDao).clearAll()
    }
}