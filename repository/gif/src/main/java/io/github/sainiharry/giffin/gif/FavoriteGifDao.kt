package io.github.sainiharry.giffin.gif

import androidx.room.*
import io.github.sainiharry.giffin.common.Gif
import kotlinx.coroutines.flow.Flow

private const val TABLE_NAME = "FavoriteGifs"

@Entity(tableName = TABLE_NAME)
internal data class FavoriteGifEntity(@PrimaryKey val id: String = "", val favorite: Boolean = true)

@Dao
internal interface FavoriteGifsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoriteGifEntity: FavoriteGifEntity)

    @Query("SELECT * FROM $TABLE_NAME INNER JOIN GifEntity ON $TABLE_NAME.id = GifEntity.id")
    fun getFavoriteGifs(): Flow<List<Gif>>

    @Delete
    suspend fun remove(favoriteGifEntity: FavoriteGifEntity)
}