package com.vorobyoff.gallery.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class CollectionResponse(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "user") val user: UserResponse,
    @field:Json(name = "preview_photos") val previews: List<PreviewResponse>
)