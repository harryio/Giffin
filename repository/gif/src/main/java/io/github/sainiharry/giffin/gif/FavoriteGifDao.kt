package io.github.sainiharry.giffin.gif

import androidx.room.*
import io.github.sainiharry.giffin.common.Gif
import kotlinx.coroutines.flow.Flow

private const val TABLE_NAME = "FavoriteGifs"

@Entity(tableName = TABLE_NAME)
data class FavoriteGifEntity(@PrimaryKey val id: String = "", val favorite: Boolean = true)

@Dao
interface FavoriteGifsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteGifEntity: FavoriteGifEntity)

    @Query("SELECT * FROM $TABLE_NAME")
    fun getFavoriteGifs(): Flow<List<Gif>>

    @Delete
    suspend fun remove(favoriteGifEntity: FavoriteGifEntity)
}