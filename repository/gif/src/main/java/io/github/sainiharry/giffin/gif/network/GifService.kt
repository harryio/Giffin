package io.github.sainiharry.giffin.gif.network

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Network Service that provides gif data from [GiphyApi](https://developers.giphy.com/docs/)
 */
internal interface GifService {

    /**
     * Fetch trending Gifs
     * @param offset starting position of the gifs to be fetched
     * @param limit maximum number of gifs to be requested in a page
     */
    @GET("/v1/gifs/trending")
    suspend fun fetchTrendingGifs(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): GifResponseWrapper?

    /**
     * Search Gifs
     * @param query search query term or phrase
     * @param offset starting position of the gifs to be fetched
     * @param limit maximum number of gif search results to be returned in a page
     */
    @GET("/v1/gifs/search")
    suspend fun searchGifs(
        @Query("q") query: String,
        @Query("offset") offset: Int,
        @Query("limit") limit:Int
    ): GifResponseWrapper?
}