package io.github.sainiharry.giffin.gif.database

import androidx.room.*
import io.github.sainiharry.giffin.common.Gif
import kotlinx.coroutines.flow.Flow

private const val TABLE_NAME = "FavoriteGifs"

/**
 * Database entity for favorite gifs
 */
@Entity(tableName = TABLE_NAME, primaryKeys = ["id"])
internal data class FavoriteGifEntity(
    @Embedded() val gifEntity: GifEntity,
    val favorite: Boolean = true
)

/**
 * DAO for Favorite Gifs
 */
@Dao
internal interface FavoriteGifsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoriteGifEntity: FavoriteGifEntity)

    @Query("SELECT * FROM $TABLE_NAME")
    fun getFavoriteGifs(): Flow<List<Gif>>

    @Query("DELETE FROM $TABLE_NAME WHERE $TABLE_NAME.id = :id")
    suspend fun remove(id: String)
}