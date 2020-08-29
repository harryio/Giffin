package io.github.sainiharry.giffin.gif.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GifEntity::class], version = 1)
internal abstract class GifDatabase : RoomDatabase() {

    abstract fun gifDao(): GifDao
}