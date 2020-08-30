package io.github.sainiharry.griffin.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import io.github.sainiharry.griffin.feature.home.databinding.FragmentHomeBinding
import kotlinx.coroutines.FlowPreview

@FlowPreview
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        binding.pager.adapter = HomePagerAdapter(requireActivity())
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.setText(R.string.trending)
                    tab.setIcon(R.drawable.ic_trending)
                }

                1 -> {
                    tab.setText(R.string.favorite)
                    tab.setIcon(R.drawable.ic_favorite)
                }
            }
        }.attach()

        binding.searchButton.setOnClickListener {
            navController.navigate(R.id.action_home_to_search)
        }
    }
}