package io.github.sainiharry.giffin.feature.trendinggifs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    val searchQuery = MutableLiveData<String>()
}