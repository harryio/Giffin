package io.github.sainiharry.giffin.gif

import retrofit2.http.GET

internal interface GifService {

    @GET("/v1/gifs/trending")
    suspend fun fetchTrendingGifs(): GifResponseWrapper?
}