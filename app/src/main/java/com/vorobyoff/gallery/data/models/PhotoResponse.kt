package com.vorobyoff.gallery.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class PhotoResponse(
        @field:Json(name = "id") val id: String,
        @field:Json(name = "width") val width: Int,
        @field:Json(name = "height") val height: Int,
        @field:Json(name = "urls") val url: UrlResponse,
        @field:Json(name = "user") val user: UserResponse,
        @field:Json(name = "links") val link: LinkResponse,
        @field:Json(name = "description") val description: String?
)