package io.github.sainiharry.giffin.gif.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.sainiharry.giffin.common.Gif

/**
 * Database representation of Gif information from network
 */
@Entity
internal data class GifEntity(
    @PrimaryKey val id: String = "",
    val url: String,
    val width: Int,
    val height: Int
)

internal fun Gif.toGifEntity() = GifEntity(id, url, width, height)