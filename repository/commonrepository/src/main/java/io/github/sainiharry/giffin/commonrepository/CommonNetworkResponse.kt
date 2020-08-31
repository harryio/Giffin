package io.github.sainiharry.giffin.commonrepository

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Pagination response from Giphy Api
 */
@JsonClass(generateAdapter = true)
data class PaginationResponse(
    @Json(name = "total_count") val totalCount: Int = 0,
    val count: Int = 0,
    val offset: Int = 0
)