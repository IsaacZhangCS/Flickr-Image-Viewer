package com.example.flickrimageviewer.data.model

import com.google.gson.annotations.SerializedName

data class PhotoListMetadataDto(
    val page: Int,
    val pages: Int,
    @SerializedName("perpage")
    val perPage: Int,
    val total: Int,
    val photo: List<PhotoDto>,
)
