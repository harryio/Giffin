package io.github.sainiharry.giffin.gif.database

import androidx.paging.PagingSource
import androidx.room.*
import io.github.sainiharry.giffin.common.Gif

private const val TABLE_NAME = "SearchedGifs"

@Entity(tableName = TABLE_NAME, primaryKeys = ["id"])
internal class SearchedGifEntity(@Embedded val gifEntity: GifEntity)

@Dao
internal interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<SearchedGifEntity>)

    @Query("SELECT * FROM SearchedGifs LEFT JOIN FavoriteGifs ON SearchedGifs.id = FavoriteGifs.id")
    fun pagingSource(): PagingSource<Int, Gif>

    @Query("DELETE FROM SearchedGifs")
    suspend fun clearAll()

    @Transaction
    suspend fun insert(list: List<SearchedGifEntity>, refresh: Boolean) {
        if (refresh) {
            clearAll()
        }

        insertAll(list)
    }
}