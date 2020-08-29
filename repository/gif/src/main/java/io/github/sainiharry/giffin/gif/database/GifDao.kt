package io.github.sainiharry.giffin.gif.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import io.github.sainiharry.giffin.common.Gif

@Dao
internal interface GifDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(gifList: List<GifEntity>)

    @Query("SELECT * FROM GifEntity LEFT JOIN FavoriteGifs ON GifEntity.id = FavoriteGifs.id")
    fun pagingSource(): PagingSource<Int, Gif>

    @Query("DELETE FROM GifEntity")
    suspend fun clearAll()

    @Transaction
    suspend fun insert(list: List<GifEntity>, refresh: Boolean) {
        if (refresh) {
            clearAll()
        }

        insertAll(list)
    }
}