package io.github.sainiharry.giffin.feature.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.github.sainiharry.giffin.common.Gif
import io.github.sainiharry.giffin.featurecommonfeature.ItemClickListener
import io.github.sainiharry.giffin.gif.GifRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest

internal class SearchViewModel(private val gifRepository: GifRepository) : ViewModel(),
    ItemClickListener<Gif> {

    val searchQuery = MutableLiveData<String>()

    @FlowPreview
    @ExperimentalCoroutinesApi
    val searchResults: Flow<PagingData<Gif>> = searchQuery.asFlow()
        .debounce(500)
        .flatMapLatest {
            gifRepository.searchGifs(it)
        }.cachedIn(viewModelScope)

    override fun onItemClick(item: Gif) {
        TODO("Not yet implemented")
    }
}