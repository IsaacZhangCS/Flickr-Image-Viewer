package com.example.flickrimageviewer.domain.usecase

import com.example.flickrimageviewer.data.repository.FlickrRepository
import com.example.flickrimageviewer.domain.model.PhotosInformation
import javax.inject.Inject

class GetPhotosFromSearchUseCase @Inject constructor(
    private val flickrRepositoryImpl: FlickrRepository
) {

    suspend operator fun invoke(
        text: String,
        page: Int,
        perPage: Int,
    ): PhotosInformation {
        println("izzz: searching with text ${text} and page ${page} and perPage ${perPage}")
        return flickrRepositoryImpl.getPhotosBySearch(
            searchQuery = text,
            page = page,
            perPage = perPage
        )
    }
}