package com.example.flickrimageviewer.data.repository

import com.example.flickrimageviewer.BuildConfig
import com.example.flickrimageviewer.data.remote.FlickrApiService
import com.example.flickrimageviewer.domain.model.PhotosInformation
import com.example.flickrimageviewer.domain.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FlickrRepositoryImpl @Inject constructor(
    private val flickrApiService: FlickrApiService
): FlickrRepository {

    override suspend fun getRecentPhotos(
        page: Int,
        perPage: Int
    ): PhotosInformation {
        return withContext(Dispatchers.IO) {
            flickrApiService.getRecentPhotos(
                apiKey = BuildConfig.FLICKR_API_KEY,
                page = page,
                perPage = perPage
            ).photos.toDomain()
        }
    }

    override suspend fun getPhotosBySearch(
        searchQuery: String,
        page: Int,
        perPage: Int
    ): PhotosInformation {
        return withContext(Dispatchers.IO) {
            flickrApiService.getPhotosBySearch(
                text = searchQuery,
                apiKey = BuildConfig.FLICKR_API_KEY,
                page = page,
                perPage = perPage
            ).photos.toDomain()
        }
    }
}