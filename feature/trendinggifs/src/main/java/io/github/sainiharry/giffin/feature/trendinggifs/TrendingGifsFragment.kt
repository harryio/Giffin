package io.github.sainiharry.giffin.feature.trendinggifs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.sainiharry.giffin.feature.trendinggifs.databinding.FragmentTrendingGifsBinding

class TrendingGifsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentTrendingGifsBinding.inflate(inflater, container, false).root
    }
}