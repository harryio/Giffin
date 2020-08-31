package io.github.sainiharry.griffin.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import io.github.sainiharry.giffin.feature.trendinggifs.SearchViewModel
import io.github.sainiharry.giffin.featurecommonfeature.hideKeyboard
import io.github.sainiharry.griffin.feature.home.databinding.FragmentHomeBinding
import kotlinx.coroutines.FlowPreview

@FlowPreview
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val searchViewModel by activityViewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.model = searchViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.toolbar.visibility = if (tab?.position == 1) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

                activity?.hideKeyboard()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
}