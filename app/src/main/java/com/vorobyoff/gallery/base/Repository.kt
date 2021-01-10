package com.vorobyoff.gallery.base

import androidx.paging.PagingData
import com.vorobyoff.gallery.data.models.OrientationParam
import com.vorobyoff.gallery.presentation.models.CollectionItem
import com.vorobyoff.gallery.presentation.models.PhotoItem
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun randomPhoto(orientation: OrientationParam): Flow<PhotoItem>

    fun photo(id: String): Flow<PhotoItem>

    fun listCollections(pageSize: Int): Flow<PagingData<CollectionItem>>

    fun collectionPhotos(pageSize: Int, id: String): Flow<PagingData<PhotoItem>>

    fun searchPhotos(pageSize: Int, query: String): Flow<PagingData<PhotoItem>>
}