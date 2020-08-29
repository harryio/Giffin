package io.github.sainiharry.giffin.gif.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.sainiharry.giffin.gif.FavoriteGifsDao
import io.github.sainiharry.giffin.gif.FavoriteGifEntity

@Database(entities = [GifEntity::class, FavoriteGifEntity::class], version = 1)
internal abstract class GifDatabase : RoomDatabase() {

    abstract fun gifDao(): GifDao

    abstract fun favoriteGifsDao(): FavoriteGifsDao
}