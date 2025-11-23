package com.example.flickrimageviewer.data.remote

import com.example.flickrimageviewer.data.model.PhotoListResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApiService {

    @GET("services/rest/")
    suspend fun getRecentPhotos(
        @Query("method") method: String = "flickr.photos.getRecent",
        @Query("api_key") apiKey: String,
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") nojsonCallback: Int = 1
    ): PhotoListResponseDto

    @GET("services/rest/")
    suspend fun getPhotosBySearch(
        @Query("text") text: String,
        @Query("method") method: String = "flickr.photos.search",
        @Query("api_key") apiKey: String,
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") nojsonCallback: Int = 1
    ): PhotoListResponseDto


}