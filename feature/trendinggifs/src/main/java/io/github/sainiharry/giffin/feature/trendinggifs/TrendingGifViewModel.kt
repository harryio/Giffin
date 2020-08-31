package io.github.sainiharry.giffin.feature.trendinggifs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import io.github.sainiharry.giffin.common.Gif
import io.github.sainiharry.giffin.featurecommonfeature.ItemClickListener
import io.github.sainiharry.giffin.gif.GifRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class TrendingGifViewModel(
    private val gifRepository: GifRepository
) : ViewModel(), ItemClickListener<Gif> {

    private val searchQuery = MutableLiveData("")

    @ExperimentalCoroutinesApi
    @FlowPreview
    val gifList = searchQuery.asFlow()
        .debounce(500)
        .onEach {
            gifRepository.clearSearchResults()
        }
        .flatMapLatest {
            if (it.isNullOrEmpty() || it.trim().isBlank()) {
                gifRepository.getTrendingGifsPager()
            } else {
                gifRepository.searchGifs(it)
            }
        }.cachedIn(viewModelScope)

    val gifListFlow = gifRepository.getTrendingGifsPager()
        .cachedIn(viewModelScope)

    override fun onItemClick(item: Gif) {
        viewModelScope.launch {
            if (item.favorite) {
                gifRepository.unFavoriteGif(item)
            } else {
                gifRepository.favoriteGif(item)
            }
        }
    }

    internal fun handleSearchQuery(query: String?) {
        searchQuery.value = query
    }
}