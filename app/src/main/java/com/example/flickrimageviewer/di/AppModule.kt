package com.example.flickrimageviewer.di

import com.example.flickrimageviewer.data.remote.FlickrApiService
import com.example.flickrimageviewer.data.repository.FlickrRepository
import com.example.flickrimageviewer.data.repository.FlickrRepositoryImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val FLICKR_API_BASE_URL = "https://www.flickr.com/"

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .setStrictness(Strictness.LENIENT)
        .create()

    @Provides
    @Singleton
    fun provideFlickrApiRetrofit(gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(FLICKR_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun provideFlickrApiService(retrofit: Retrofit): FlickrApiService =
        retrofit.create(FlickrApiService::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindFlickrRepository(
        impl: FlickrRepositoryImpl
    ): FlickrRepository
}