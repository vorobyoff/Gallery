package com.vorobyoff.gallery.data.datasources

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vorobyoff.gallery.data.models.CollectionResponse
import com.vorobyoff.gallery.data.models.OrientationParam
import com.vorobyoff.gallery.data.models.PhotoResponse
import com.vorobyoff.gallery.data.networking.UnsplashApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class NetworkDataSource(private val api: UnsplashApi) {

    fun randomPhoto(orientation: OrientationParam): Flow<PhotoResponse> =
        flow { emit(api.randomPhoto(orientation.param)) }.flowOn(Dispatchers.IO)

    fun photo(id: String): Flow<PhotoResponse> = flow { emit(api.photo(id)) }.flowOn(Dispatchers.IO)

    fun listCollections(pageSize: Int): Flow<PagingData<CollectionResponse>> =
        Pager(PagingConfig(pageSize = pageSize)) { CollectionPagingSource(api) }.flow
            .flowOn(Dispatchers.IO)

    fun collectionPhotos(pageSize: Int, id: String): Flow<PagingData<PhotoResponse>> =
        Pager(PagingConfig(pageSize)) { PhotoPagingSource(api = api, id = id) }.flow
            .flowOn(Dispatchers.IO)

    fun searchPhotos(pageSize: Int, query: String): Flow<PagingData<PhotoResponse>> =
        Pager(PagingConfig(pageSize)) { SearchPhotosPagingSource(api = api, query = query) }.flow
            .flowOn(Dispatchers.IO)
}