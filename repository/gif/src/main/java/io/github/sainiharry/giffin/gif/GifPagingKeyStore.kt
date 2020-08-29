package io.github.sainiharry.giffin.gif

import android.content.SharedPreferences
import io.github.sainiharry.giffin.commonrepository.PACKAGE_NAME

private const val KEY_GIF_PAGING = "$PACKAGE_NAME.KEY_GIF_PAGING"

interface GifPagingKeyStore {

    fun getKey(): Int?

    fun setKey(newKey: Int)
}

internal class SharedPrefPagingKeyStore(private val sharedPreferences: SharedPreferences) :
    GifPagingKeyStore {

    override fun getKey(): Int? = if (sharedPreferences.contains(KEY_GIF_PAGING)) {
        sharedPreferences.getInt(KEY_GIF_PAGING, 0)
    } else {
        null
    }

    override fun setKey(newKey: Int) {
        sharedPreferences.edit().putInt(KEY_GIF_PAGING, newKey).apply()
    }
}