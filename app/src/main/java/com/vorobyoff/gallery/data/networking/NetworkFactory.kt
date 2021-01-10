package com.vorobyoff.gallery.data.networking

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Converter.Factory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object NetworkFactory {
    private const val BASE_URL = "https://api.unsplash.com"
    private const val API_KEY = "7nls_XhvpUqqKmN4IsYWZyQtUkg6M_R9U5OnzBPR7CE"

    val UNSPLASH_API: UnsplashApi by lazy {
        Retrofit.Builder()
            .addConverterFactory(converter())
            .baseUrl(BASE_URL)
            .client(client())
            .build().create()
    }

    private fun converter(): Factory = MoshiConverterFactory.create()

    private fun client(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor())
        .addInterceptor(requestInterceptor())
        .retryOnConnectionFailure(true)
        .build()

    private fun loggingInterceptor(): Interceptor = HttpLoggingInterceptor().setLevel(BODY)

    private fun requestInterceptor(): Interceptor = Interceptor {
        val old: Request = it.request()
        val url: HttpUrl = old.url.newBuilder().addQueryParameter("client_id", API_KEY).build()
        it.proceed(old.newBuilder().url(url).build())
    }
}