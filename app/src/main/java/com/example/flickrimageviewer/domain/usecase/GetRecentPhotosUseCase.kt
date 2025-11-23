package com.example.flickrimageviewer.domain.usecase

import com.example.flickrimageviewer.data.repository.FlickrRepository
import com.example.flickrimageviewer.domain.model.PhotosInformation
import javax.inject.Inject

class GetRecentPhotosUseCase @Inject constructor(
    private val flickrRepositoryImpl: FlickrRepository
) {

    suspend operator fun invoke(
        page: Int,
        perPage: Int,
    ): PhotosInformation {
        return flickrRepositoryImpl.getRecentPhotos(
            page = page,
            perPage = perPage
        )
    }
}