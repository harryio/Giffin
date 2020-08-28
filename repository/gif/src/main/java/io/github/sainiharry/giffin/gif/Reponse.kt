package io.github.sainiharry.giffin.gif

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import io.github.sainiharry.giffin.common.Gif

@JsonClass(generateAdapter = true)
internal data class GifResponseWrapper(val data: List<GifResponse?>?)

@JsonClass(generateAdapter = true)
internal data class GifResponse(
    val id: String?,
    val type: String?,
    @Json(name = "preview_gif") val gif: GifImage?
)

internal data class GifImage(val url: String?, val height: Int = 0, val width: Int = 0)

internal fun String?.isValid(): Boolean {
    return !isNullOrEmpty() && !isEmpty()
}

internal fun GifImage?.isValid(): Boolean = this?.url.isValid()

internal fun GifResponse?.toGif(): Gif? {
    if (this == null) {
        return null
    }

    return if (id.isValid() && type.isValid() && gif.isValid()) {
        Gif(id!!, gif?.url!!, gif.height, gif.width)
    } else {
        null
    }
}