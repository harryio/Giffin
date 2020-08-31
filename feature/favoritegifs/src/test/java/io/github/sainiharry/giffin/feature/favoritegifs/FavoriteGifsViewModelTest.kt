package io.github.sainiharry.giffin.feature.favoritegifs

import io.github.sainiharry.giffin.common.Gif
import io.github.sainiharry.giffin.gif.GifRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FavoriteGifsViewModelTest {

    private lateinit var gifRepository: GifRepository

    @Test
    fun testInit() {
        getViewModel()
        verify(gifRepository).getFavoriteGifs()
    }

    @Test
    fun testOnItemClickWhenGifIsNotFavorite() = runBlockingTest {
        val viewModel = getViewModel()
        val gif = Gif("id", "https:///gifurl.com", 120, 120)
        viewModel.onItemClick(gif)
        verify(gifRepository).getFavoriteGifs()
        verifyNoMoreInteractions(gifRepository)
    }

    @Test
    fun testOnItemClickWhenGifIsFavorited() = runBlockingTest {
        val viewModel = getViewModel()
        val gif = Gif("id", "https:///gifurl.com", 120, 120, true)
        viewModel.onItemClick(gif)
        verify(gifRepository).getFavoriteGifs()
        verify(gifRepository).unFavoriteGif(gif)
        verifyNoMoreInteractions(gifRepository)
    }


    private fun getViewModel(): FavoriteGifsViewModel {
        gifRepository = Mockito.mock(GifRepository::class.java)
        return FavoriteGifsViewModel(
            gifRepository,
            TestCoroutineDispatcher()
        )
    }
}