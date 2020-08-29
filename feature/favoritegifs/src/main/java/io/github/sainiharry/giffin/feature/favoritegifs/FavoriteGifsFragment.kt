package io.github.sainiharry.giffin.feature.favoritegifs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.sainiharry.giffin.feature.favoritegifs.databinding.FragmentFavoriteGifsBinding
import org.koin.android.ext.android.get

class FavoriteGifsFragment : Fragment() {

    private val viewModel by viewModels<FavoriteGifsViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return FavoriteGifsViewModel(get()) as T
            }
        }
    }

    private lateinit var binding: FragmentFavoriteGifsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteGifsBinding.inflate(inflater, container, false)
        return binding.root
    }
}