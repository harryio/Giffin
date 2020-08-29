package io.github.sainiharry.giffin.gif

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import io.github.sainiharry.giffin.common.Gif
import io.github.sainiharry.giffin.commonrepository.PaginationResponse

@JsonClass(generateAdapter = true)
internal data class GifResponseWrapper(
    val data: List<GifResponse?>?,
    val pagination: PaginationResponse?
)

@JsonClass(generateAdapter = true)
internal data class GifResponse(
    val id: String?,
    val images: ImagesWrapper?
)

@JsonClass(generateAdapter = true)
internal data class ImagesWrapper(@Json(name = "fixed_height") val gif: GifImage?)

@JsonClass(generateAdapter = true)
internal data class GifImage(val url: String?, val height: Int = 0, val width: Int = 0)

internal fun String?.isValid(): Boolean {
    return !isNullOrEmpty() && !isEmpty()
}

internal fun ImagesWrapper?.isValid(): Boolean = this?.gif?.url.isValid()

internal fun GifResponse?.toGif(): Gif? {
    if (this == null) {
        return null
    }

    return if (id.isValid() && images.isValid()) {
        Gif(id!!, images!!.gif!!.url!!, images.gif!!.height, images.gif.width)
    } else {
        null
    }
}