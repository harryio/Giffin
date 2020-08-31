package io.github.sainiharry.giffin.gif.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * App database that stores gif data
 */
@Database(entities = [GifEntity::class, FavoriteGifEntity::class, SearchedGifEntity::class], version = 1)
internal abstract class GifDatabase : RoomDatabase() {

    abstract fun gifDao(): GifDao

    abstract fun favoriteGifsDao(): FavoriteGifsDao

    abstract fun seachedGifsDao(): SearchDao
}