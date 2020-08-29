package io.github.sainiharry.giffin.feature.trendinggifs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.github.sainiharry.giffin.feature.trendinggifs.databinding.FragmentTrendingGifsBinding
import io.github.sainiharry.giffin.featurecommonfeature.GifAdapter
import io.github.sainiharry.giffin.featurecommonfeature.LoadingAdapter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

@FlowPreview
class TrendingGifsFragment : Fragment() {

    private val viewModel by viewModels<TrendingGifViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TrendingGifViewModel(get()) as T
            }
        }
    }

    private lateinit var binding: FragmentTrendingGifsBinding

    private lateinit var pagingAdapter: GifAdapter

    private lateinit var adapter: ConcatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrendingGifsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!this::adapter.isInitialized) {
            pagingAdapter = GifAdapter(viewModel)
            adapter = pagingAdapter.withLoadStateFooter(LoadingAdapter {
                context?.let {
                    Snackbar.make(
                        binding.root,
                        "Error while fetching gif page from network",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            })
        }

        val spanCount = 2
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), spanCount)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        binding.refreshLayout.setOnRefreshListener {
            pagingAdapter.refresh()
        }

        lifecycleScope.launch {
            viewModel.gifListFlow.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }

        lifecycleScope.launch {
            pagingAdapter.loadStateFlow.debounce(100).collectLatest { loadStates ->
                binding.refreshLayout.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }
    }
}