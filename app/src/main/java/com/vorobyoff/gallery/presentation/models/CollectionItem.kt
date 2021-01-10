package com.vorobyoff.gallery.presentation.models

data class CollectionItem(
    val id: String = "",
    val title: String = "",
    val username: String = "",
    val profileImg: String = "",
    val previewItems: List<PreviewItem> = emptyList()
)