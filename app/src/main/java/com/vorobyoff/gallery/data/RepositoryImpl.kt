package com.vorobyoff.gallery.data

import androidx.paging.PagingData
import androidx.paging.map
import com.vorobyoff.gallery.base.Repository
import com.vorobyoff.gallery.data.datasources.NetworkDataSource
import com.vorobyoff.gallery.data.models.CollectionResponse
import com.vorobyoff.gallery.data.models.OrientationParam
import com.vorobyoff.gallery.presentation.models.CollectionItem
import com.vorobyoff.gallery.presentation.models.PhotoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.vorobyoff.gallery.data.CollectionResponseToCollectionItemMapper.map as collectionMapper
import com.vorobyoff.gallery.data.PhotoResponseToPhotoItemMapper.map as photoMapper

class RepositoryImpl(private val dataSource: NetworkDataSource) : Repository {

    override fun randomPhoto(orientation: OrientationParam): Flow<PhotoItem> =
        dataSource.randomPhoto(orientation).map { photoMapper(it) }

    override fun photo(id: String): Flow<PhotoItem> = dataSource.photo(id).map { photoMapper(it) }

    override fun listCollections(pageSize: Int): Flow<PagingData<CollectionItem>> =
        dataSource.listCollections(pageSize).map { data: PagingData<CollectionResponse> ->
            data.map { collectionMapper(it) }
        }

    override fun collectionPhotos(pageSize: Int, id: String): Flow<PagingData<PhotoItem>> =
        dataSource.collectionPhotos(pageSize = pageSize, id = id).map { data ->
            data.map { photoMapper(it) }
        }

    override fun searchPhotos(pageSize: Int, query: String): Flow<PagingData<PhotoItem>> =
        dataSource.searchPhotos(pageSize = pageSize, query = query).map { data ->
            data.map { photoMapper(it) }
        }
}