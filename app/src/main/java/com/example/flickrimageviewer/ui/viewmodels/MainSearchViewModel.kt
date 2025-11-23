package com.example.flickrimageviewer.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickrimageviewer.domain.model.Photo
import com.example.flickrimageviewer.domain.usecase.GetPhotosFromSearchUseCase
import com.example.flickrimageviewer.domain.usecase.GetRecentPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainSearchViewModel @Inject constructor(
    private val getPhotosFromSearchUseCase: GetPhotosFromSearchUseCase,
    private val getRecentPhotosUseCase: GetRecentPhotosUseCase
): ViewModel() {

    private var _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private var _uiEvent: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val uiEvent = _uiEvent.asSharedFlow()

    private var searchJob: Job? = null

    fun updateSearchQuery(query: String) {
        _uiState.update {
            it.copy(searchQuery = query)
        }
    }

    fun onSearchQueryExecuted() {
        val trimmedQuery = _uiState.value.searchQuery.trim()
        val currentState = _uiState.value
        // Ignoring searching recent photos on empty search, don't search duplicate
        if (currentState.photos.isNotEmpty() && trimmedQuery == currentState.lastSearchedQuery) {
            return
        }

        // Reset the state on a new search executing
        _uiState.update {
            it.copy(
                photos = emptyList(),
                searchQuery = trimmedQuery,
                lastSearchedQuery = trimmedQuery,
                currentPage = 0,
                totalPages = 100,
                fetchingInitialPhotos = true
            )
        }
        fetchPhotos()
    }

    // Fetches photos
    // Supports both initial fetch and fetching more
    fun fetchPhotos() {
        searchJob?.cancel()
        val currentQuery = _uiState.value.searchQuery
        val pageToFetch = _uiState.value.currentPage + 1
        val totalPages = _uiState.value.totalPages
        if (pageToFetch > totalPages) {
            _uiState.update { it.copy(fetchingInitialPhotos = false) }
            return
        }

        searchJob = viewModelScope.launch {
            try {
                val photoResponse = if (currentQuery.isNotBlank()) {
                    getPhotosFromSearchUseCase(
                        text = currentQuery,
                        page = pageToFetch,
                        perPage = ITEM_LIMIT
                    )
                } else {
                    getRecentPhotosUseCase(
                        page = pageToFetch,
                        perPage = ITEM_LIMIT
                    )
                }
                _uiState.update {
                    it.copy(
                        photos = it.photos + photoResponse.photos.map { photo -> getUrlStringFromPhoto(photo) },
                        currentPage = photoResponse.page,
                        totalPages = photoResponse.pages,
                        fetchingInitialPhotos = false
                    )
                }
            } catch(e: Exception) {
                _uiEvent.tryEmit(UiEvent.OnError)
                _uiState.update { it.copy( fetchingInitialPhotos = false) }
            }
        }
    }

    private fun getUrlStringFromPhoto(photo: Photo): String {
        return photo.serverId + "/" + photo.id + "_" + photo.secret + "_" + MEDIUM_SUFFIX_SIZE + "." + PHOTO_FORMAT
    }

    data class UiState(
        val searchQuery: String = "",
        val lastSearchedQuery: String = "",
        val photos: List<String> = emptyList(),
        val currentPage: Int = 0,
        val totalPages: Int = 100,
        val fetchingInitialPhotos: Boolean = false,
    )

    sealed class UiEvent {
        data object OnError: UiEvent()
    }

    companion object {
        const val ITEM_LIMIT = 100
        const val MEDIUM_SUFFIX_SIZE = "m"
        const val PHOTO_FORMAT = "jpg"
    }

}