package io.github.sainiharry.giffin.feature.favoritegifs

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import io.github.sainiharry.giffin.common.Gif
import io.github.sainiharry.giffin.featurecommonfeature.GifViewHolder
import io.github.sainiharry.giffin.featurecommonfeature.ItemClickListener
import io.github.sainiharry.giffin.featurecommonfeature.gifListDiffer

class FavoriteGifsAdapter(private val itemClickListener: ItemClickListener<Gif>) :
    ListAdapter<Gif, GifViewHolder>(gifListDiffer) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder =
        GifViewHolder(parent, itemClickListener)

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: GifViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            holder.updateFavorite(getItem(position))
        } else {
            onBindViewHolder(holder, position)
        }
    }
}