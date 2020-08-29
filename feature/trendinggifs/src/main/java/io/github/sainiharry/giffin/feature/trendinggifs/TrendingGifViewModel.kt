package io.github.sainiharry.giffin.feature.trendinggifs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import io.github.sainiharry.giffin.gif.GifRepository

internal class TrendingGifViewModel(
    gifRepository: GifRepository
) : ViewModel() {

    val gifListFlow = gifRepository.getTrendingGifsPager().flow.cachedIn(viewModelScope)
}