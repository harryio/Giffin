package io.github.sainiharry.griffin.feature.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.github.sainiharry.giffin.feature.favoritegifs.FavoriteGifsFragment
import io.github.sainiharry.giffin.feature.trendinggifs.TrendingGifsFragment
import kotlinx.coroutines.FlowPreview

@FlowPreview
internal class HomePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> TrendingGifsFragment()
        1 -> FavoriteGifsFragment()
        else -> throw IllegalStateException("No fragment found for position $position")
    }
}