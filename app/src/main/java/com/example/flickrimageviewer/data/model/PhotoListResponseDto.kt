package com.example.flickrimageviewer.data.model

data class PhotoListResponseDto(
    val photos: PhotoListMetadataDto,
    val stat: String? = null
)
