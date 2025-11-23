package com.example.flickrimageviewer.domain.model

import com.example.flickrimageviewer.data.model.PhotoDto
import com.example.flickrimageviewer.data.model.PhotoListMetadataDto
import com.google.gson.annotations.SerializedName

data class PhotosInformation(
    val page: Int,
    val pages: Int,
    val photos: List<Photo>
)

fun PhotoListMetadataDto.toDomain(): PhotosInformation {
    return PhotosInformation(
        page = this.page,
        pages = this.pages,
        photos = this.photo.map { it.toDomain() }
    )
}

data class Photo(
    val serverId: String,
    val id: String,
    val secret: String,
)


fun PhotoDto.toDomain(): Photo {
    return Photo(
        serverId = this.server,
        id = this.id,
        secret = this.secret
    )
}