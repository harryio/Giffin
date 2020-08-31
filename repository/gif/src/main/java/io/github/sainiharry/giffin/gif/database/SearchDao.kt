package io.github.sainiharry.giffin.gif.database

import androidx.paging.PagingSource
import androidx.room.*
import io.github.sainiharry.giffin.common.Gif

private const val TABLE_NAME = "SearchedGifs"

/**
 * Database representation of a searched gif
 */
@Entity(tableName = TABLE_NAME, primaryKeys = ["id"])
internal data class SearchedGifEntity(@Embedded val gifEntity: GifEntity)

/**
 * DAO for searched Gifs
 */
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