package com.vorobyoff.gallery.data

import com.vorobyoff.gallery.base.Mapper
import com.vorobyoff.gallery.data.models.CollectionResponse
import com.vorobyoff.gallery.data.models.PhotoResponse
import com.vorobyoff.gallery.data.models.PreviewResponse
import com.vorobyoff.gallery.presentation.models.CollectionItem
import com.vorobyoff.gallery.presentation.models.PhotoItem
import com.vorobyoff.gallery.presentation.models.PreviewItem
import com.vorobyoff.gallery.data.PreviewResponseToPreviewItemMapper.map as previewsMapper

object CollectionResponseToCollectionItemMapper : Mapper<CollectionResponse, CollectionItem> {
    override fun map(type: CollectionResponse?): CollectionItem = type?.let { response ->
        CollectionItem(
            id = response.id,
            title = response.title,
            username = response.user.username,
            profileImg = response.user.profileImg.imgUrl,
            previewItems = response.previews.map { previewsMapper(it) }
        )
    } ?: CollectionItem()
}

object PreviewResponseToPreviewItemMapper : Mapper<PreviewResponse, PreviewItem> {
    override fun map(type: PreviewResponse?): PreviewItem = type?.let {
        PreviewItem(id = it.id, imgUrl = it.url.url)
    } ?: PreviewItem()
}

object PhotoResponseToPhotoItemMapper : Mapper<PhotoResponse, PhotoItem> {
    override fun map(type: PhotoResponse?): PhotoItem = type?.let {
        PhotoItem(
            id = it.id,
            width = it.width,
            url = it.url.url,
            height = it.height,
            name = it.user.name,
            link = it.link.link,
            username = it.user.username,
            description = it.description,
            profileImg = it.user.profileImg.imgUrl,
        )
    } ?: PhotoItem()
}