package io.github.sainiharry.giffin.gif.network

import retrofit2.http.GET
import retrofit2.http.Query

internal interface GifService {

    @GET("/v1/gifs/trending")
    suspend fun fetchTrendingGifs(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): GifResponseWrapper?
}