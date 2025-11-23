package com.example.flickrimageviewer.data.repository

import com.example.flickrimageviewer.domain.model.PhotosInformation

interface FlickrRepository {

    suspend fun getRecentPhotos(
        page: Int,
        perPage: Int
    ): PhotosInformation


    suspend fun getPhotosBySearch(
        searchQuery: String,
        page: Int,
        perPage: Int
    ): PhotosInformation
}