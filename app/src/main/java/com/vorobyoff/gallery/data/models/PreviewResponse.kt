package com.vorobyoff.gallery.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class PreviewResponse(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "urls") val url: UrlResponse
)