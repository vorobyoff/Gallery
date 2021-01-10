package com.vorobyoff.gallery.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ProfileImgResponse(@field:Json(name = "medium") val imgUrl: String)