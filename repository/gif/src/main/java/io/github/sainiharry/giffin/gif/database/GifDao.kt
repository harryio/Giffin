package io.github.sainiharry.giffin.gif.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.github.sainiharry.giffin.common.Gif

@Dao
interface GifDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(gifList: List<Gif>)

    @Query("SELECT * FROM GifEntity")
    fun pagingSource(): PagingSource<Int, Gif>

    @Query("DELETE FROM GifEntity")
    suspend fun clearAll()
}