package com.vorobyoff.gallery.data.datasources

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.Error
import androidx.paging.PagingSource.LoadResult.Page
import com.vorobyoff.gallery.data.exceptions.catchNetworkException
import com.vorobyoff.gallery.data.models.CollectionResponse
import com.vorobyoff.gallery.data.networking.UnsplashApi

class CollectionPagingSource(private val api: UnsplashApi) : PagingSource<Int, CollectionResponse>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CollectionResponse> = try {
        val pageKey: Int = params.key ?: 1
        val prevKey: Int? = if (pageKey == 1) null else pageKey - 1
        val response: List<CollectionResponse> = api.listCollections(pageKey)
        Page(data = response, prevKey = prevKey, nextKey = pageKey + 1)
    } catch (exception: Exception) {
        Error(catchNetworkException(exception))
    }
}