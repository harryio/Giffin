package io.github.sainiharry.giffin.gif.paging

import android.content.SharedPreferences
import io.github.sainiharry.giffin.commonrepository.PACKAGE_NAME

private const val KEY_GIF_PAGING = "$PACKAGE_NAME.KEY_GIF_PAGING"

/**
 * A local store that stores the key for a paged data stream
 */
interface GifPagingKeyStore {

    /**
     * Get the paging key from the backing store
     */
    fun getKey(): Int?

    /**
     * Update the paging key in the backing store
     */
    fun setKey(newKey: Int)
}

/**
 * Internal implementation of [GifPagingKeyStore] that uses [SharedPreferences] as a store for
 * storing paging keys
 */
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