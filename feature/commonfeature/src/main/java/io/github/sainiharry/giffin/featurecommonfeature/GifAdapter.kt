package io.github.sainiharry.giffin.featurecommonfeature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.sainiharry.giffin.common.Gif
import io.github.sainiharry.giffin.featurecommonfeature.databinding.ItemGifBinding

private val listDiffer = object : DiffUtil.ItemCallback<Gif>() {
    override fun areItemsTheSame(oldItem: Gif, newItem: Gif): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Gif, newItem: Gif): Boolean {
        return oldItem == newItem
    }
}

class GifAdapter : PagingDataAdapter<Gif, GifViewHolder>(listDiffer) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder =
        GifViewHolder(parent)

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class GifViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_gif, parent, false)
) {

    private val binding = ItemGifBinding.bind(itemView)

    internal fun bind(gif: Gif?) {
        if (gif == null) {
            Glide.with(binding.root).clear(binding.gifImage)
        } else {
            Glide.with(binding.root)
                .load(gif.url)
                .into(binding.gifImage)
        }
        binding.executePendingBindings()
    }
}

