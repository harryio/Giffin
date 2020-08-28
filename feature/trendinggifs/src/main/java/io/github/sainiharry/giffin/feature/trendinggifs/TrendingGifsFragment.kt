package io.github.sainiharry.giffin.feature.trendinggifs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import io.github.sainiharry.giffin.feature.trendinggifs.databinding.FragmentTrendingGifsBinding
import io.github.sainiharry.giffin.featurecommonfeature.GifAdapter
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.android.get

class TrendingGifsFragment : Fragment() {

    private val viewModel by viewModels<TrendingGifViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TrendingGifViewModel(get(), Dispatchers.Main.immediate) as T
            }
        }
    }

    private lateinit var binding: FragmentTrendingGifsBinding

    private lateinit var adapter: GifAdapter

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
            adapter = GifAdapter()
        }

        val spanCount = 2
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), spanCount)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        viewModel.gifList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}