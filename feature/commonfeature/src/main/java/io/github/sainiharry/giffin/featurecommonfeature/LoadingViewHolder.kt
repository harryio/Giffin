package io.github.sainiharry.giffin.featurecommonfeature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.sainiharry.giffin.featurecommonfeature.databinding.ItemLoadingBinding

class LoadingAdapter(private val errorHandler: () -> Unit) : LoadStateAdapter<LoadingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingViewHolder =
        LoadingViewHolder(parent, errorHandler)

    override fun onBindViewHolder(holder: LoadingViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}

class LoadingViewHolder(parent: ViewGroup, private val errorHandler: () -> Unit) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
    ) {

    private val binding = ItemLoadingBinding.bind(itemView)
    private val progressBar = binding.loader

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            errorHandler()
        }

        progressBar.isVisible = loadState is LoadState.Loading
    }
}