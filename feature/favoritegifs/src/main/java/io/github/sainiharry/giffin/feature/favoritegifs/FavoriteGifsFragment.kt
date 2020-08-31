package io.github.sainiharry.giffin.feature.favoritegifs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import io.github.sainiharry.giffin.feature.favoritegifs.databinding.FragmentFavoriteGifsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class FavoriteGifsFragment : Fragment() {

    private val viewModel by viewModels<FavoriteGifsViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return FavoriteGifsViewModel(get(), Dispatchers.Main.immediate) as T
            }
        }
    }

    private lateinit var binding: FragmentFavoriteGifsBinding

    private lateinit var adapter: FavoriteGifsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteGifsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!this::adapter.isInitialized) {
            adapter = FavoriteGifsAdapter(viewModel)
        }

        val spanCount = 2
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), spanCount)
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.favoriteGifs.collectLatest {
                adapter.submitList(it)
            }
        }
    }
}