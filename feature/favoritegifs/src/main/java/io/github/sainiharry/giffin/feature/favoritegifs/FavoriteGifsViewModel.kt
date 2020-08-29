package io.github.sainiharry.giffin.feature.favoritegifs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.sainiharry.giffin.common.Gif
import io.github.sainiharry.giffin.featurecommonfeature.ItemClickListener
import io.github.sainiharry.giffin.gif.GifRepository
import kotlinx.coroutines.launch

internal class FavoriteGifsViewModel(private val gifRepository: GifRepository) : ViewModel(),
    ItemClickListener<Gif> {

    internal val favoriteGifs = gifRepository.getFavoriteGifs()

    override fun onItemClick(item: Gif) {
        viewModelScope.launch {
            if (item.favorite) {
                gifRepository.unFavoriteGif(item)
            }
        }
    }
}