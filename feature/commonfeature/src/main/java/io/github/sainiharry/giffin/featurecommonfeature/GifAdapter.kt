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

    private val PAYLOAD_FAVORITE = Any()

    override fun areItemsTheSame(oldItem: Gif, newItem: Gif): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Gif, newItem: Gif): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Gif, newItem: Gif): Any? {
        return if (oldItem.favorite != newItem.favorite) {
            PAYLOAD_FAVORITE
        } else {
            null
        }
    }
}

class GifAdapter(private val itemClickListener: ItemClickListener<Gif>) :
    PagingDataAdapter<Gif, GifViewHolder>(listDiffer) {

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

class GifViewHolder(parent: ViewGroup, private val itemClickListener: ItemClickListener<Gif>) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_gif, parent, false)
    ) {

    private val binding = ItemGifBinding.bind(itemView)

    private var gif: Gif? = null

    init {
        binding.favoriteButton.setOnClickListener {
            gif?.let {
                itemClickListener.onItemClick(it)
            }
        }
    }

    internal fun bind(gif: Gif?) {
        this.gif = gif
        if (gif == null) {
            Glide.with(binding.root).clear(binding.gifImage)
        } else {
            Glide.with(binding.root)
                .load(gif.url)
                .into(binding.gifImage)
            binding.gif = gif
        }
        binding.executePendingBindings()
    }

    internal fun updateFavorite(gif: Gif?) {
        this.gif = gif
        gif?.let {
            val resource = if (it.favorite) {
                R.drawable.ic_favorite
            } else {
                R.drawable.ic_unfavorite
            }
            binding.favoriteButton.setImageResource(resource)
        }
    }
}

