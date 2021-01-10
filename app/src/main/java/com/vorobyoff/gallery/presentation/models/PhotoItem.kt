package com.vorobyoff.gallery.presentation.models

data class PhotoItem(
    val width: Int = 0,
    val height: Int = 0,
    val id: String = "",
    val url: String = "",
    val link: String = "",
    val name: String = "",
    val username: String = "",
    val profileImg: String = "",
    val description: String? = null
)