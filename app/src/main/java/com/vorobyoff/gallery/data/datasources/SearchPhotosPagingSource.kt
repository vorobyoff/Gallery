package com.vorobyoff.gallery.data.datasources

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.Error
import androidx.paging.PagingSource.LoadResult.Page
import com.vorobyoff.gallery.data.exceptions.catchNetworkException
import com.vorobyoff.gallery.data.models.PhotoResponse
import com.vorobyoff.gallery.data.models.SearchPhotosResponse
import com.vorobyoff.gallery.data.networking.UnsplashApi

class SearchPhotosPagingSource(private val api: UnsplashApi, private val query: String) : PagingSource<Int, PhotoResponse>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoResponse> = try {
        val pageKey: Int = params.key ?: 1
        val prevKey: Int? = if (pageKey == 1) null else pageKey - 1
        val response: SearchPhotosResponse = api.searchPhotos(query = query, pageKey = pageKey)
        Page(data = response.photos, prevKey = prevKey, nextKey = pageKey + 1)
    } catch (exception: Exception) {
        Error(catchNetworkException(exception))
    }
}