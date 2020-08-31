package io.github.sainiharry.giffin.feature.trendinggifs

import io.github.sainiharry.giffin.common.Gif
import io.github.sainiharry.giffin.gif.GifRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class TrendingGifViewModelTest {

    private lateinit var gifRepository: GifRepository

    private lateinit var model: TrendingGifViewModel

    @Before
    fun setup() {
        gifRepository = mock(GifRepository::class.java)
        model = TrendingGifViewModel(gifRepository, TestCoroutineDispatcher())
    }

    @Test
    fun testOnItemClickWithFavoriteGif() = runBlockingTest {
        val gif = Gif("id", "http://gifurl.com", 120, 120, true)
        model.onItemClick(gif)
        verify(gifRepository).unFavoriteGif(gif)
        verifyNoMoreInteractions(gifRepository)
    }

    @Test
    fun testOnItemClickWithUnFavoriteGif() = runBlockingTest {
        val gif = Gif("id", "http://gifurl.com", 120, 120)
        model.onItemClick(gif)
        verify(gifRepository).favoriteGif(gif)
        verifyNoMoreInteractions(gifRepository)
    }
}