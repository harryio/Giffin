package io.github.sainiharry.giffin.gif.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class GifEntity(
    @PrimaryKey val id: String = "",
    val url: String,
    val width: Int,
    val height: Int
)