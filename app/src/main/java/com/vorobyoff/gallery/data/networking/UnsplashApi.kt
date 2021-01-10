package com.vorobyoff.gallery.data.networking

import com.vorobyoff.gallery.data.models.CollectionResponse
import com.vorobyoff.gallery.data.models.PhotoResponse
import com.vorobyoff.gallery.data.models.SearchPhotosResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashApi {

    @GET("/photos/random")
    suspend fun randomPhoto(@Query("orientation") orientation: String): PhotoResponse

    @GET("/photos/{id}")
    suspend fun photo(@Path("id") id: String): PhotoResponse

    @GET("/collections")
    suspend fun listCollections(@Query("page") pageKey: Int): List<CollectionResponse>

    @GET("/collections/{id}/photos")
    suspend fun collectionPhotos(
        @Path("id") id: String,
        @Query("page") pageKey: Int
    ): List<PhotoResponse>

    @GET("/search/photos")
    suspend fun searchPhotos(
        @Query("page") pageKey: Int,
        @Query("query") query: String
    ): SearchPhotosResponse
}