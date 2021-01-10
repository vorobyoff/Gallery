package com.vorobyoff.gallery.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SearchPhotosResponse(@field:Json(name = "results") val photos: List<PhotoResponse>)