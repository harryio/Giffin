package io.github.sainiharry.giffin.gif.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.sainiharry.giffin.common.Gif

@Entity
internal data class GifEntity(
    @PrimaryKey val id: String = "",
    val url: String,
    val width: Int,
    val height: Int
)

internal fun GifEntity.toGif(): Gif = Gif(id, url, width, height)