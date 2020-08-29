package io.github.sainiharry.giffin.feature.trendinggifs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import io.github.sainiharry.giffin.common.Gif
import io.github.sainiharry.giffin.featurecommonfeature.ItemClickListener
import io.github.sainiharry.giffin.gif.GifRepository
import kotlinx.coroutines.launch

internal class TrendingGifViewModel(
    private val gifRepository: GifRepository
) : ViewModel(), ItemClickListener<Gif> {

    val gifListFlow = gifRepository.getTrendingGifsPager().cachedIn(viewModelScope)

    override fun onItemClick(item: Gif) {
        viewModelScope.launch {
            if (item.favorite) {
                gifRepository.unFavoriteGif(item)
            } else {
                gifRepository.favoriteGif(item)
            }
        }
    }
}