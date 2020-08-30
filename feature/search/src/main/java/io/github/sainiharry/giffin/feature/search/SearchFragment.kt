package io.github.sainiharry.giffin.feature.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.github.sainiharry.giffin.feature.search.databinding.FragmentSearchBinding
import io.github.sainiharry.giffin.featurecommonfeature.GifAdapter
import io.github.sainiharry.giffin.featurecommonfeature.LoadingAdapter
import io.github.sainiharry.giffin.featurecommonfeature.focusAndShowKeyboard
import io.github.sainiharry.giffin.featurecommonfeature.hideKeyboard
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private lateinit var pagingAdapter: GifAdapter

    private lateinit var adapter: ConcatAdapter

    private val viewModel by viewModels<SearchViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SearchViewModel(get()) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.model = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

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

        lifecycleScope.launch {
            viewModel.searchResults.collectLatest {
                pagingAdapter.submitData(it)
            }
        }

        binding.searchField.focusAndShowKeyboard()
    }

    override fun onDestroyView() {
        requireActivity().hideKeyboard()
        super.onDestroyView()
    }
}