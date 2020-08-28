package io.github.sainiharry.giffin.feature.trendinggifs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.sainiharry.giffin.common.Gif
import io.github.sainiharry.giffin.gif.GifRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class TrendingGifViewModel(
    private val gifRepository: GifRepository,
    private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _gifList = MutableLiveData<List<Gif>>()
    internal val gifList: LiveData<List<Gif>>
        get() = _gifList

    init {
        viewModelScope.launch(coroutineDispatcher) {
            _gifList.value = gifRepository.fetchTrendingGifs()
        }
    }
}