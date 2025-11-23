package com.example.flickrimageviewer.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.flickrimageviewer.core.constants.Constants
import com.example.flickrimageviewer.ui.viewmodels.MainSearchViewModel

@Composable
fun MainSearchScreen(
    viewModel: MainSearchViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.uiState.collectAsState()
    val lazyGridState = rememberLazyGridState()
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                MainSearchViewModel.UiEvent.OnError -> {
                    snackbarHostState.showSnackbar(
                        message = "Error loading photos. Please try again.",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleItem ->
                val totalItems = lazyGridState.layoutInfo.totalItemsCount
                lastVisibleItem?.let {
                    if (it > 0 && totalItems > 0 && it > totalItems - 10) {
                        viewModel.fetchPhotos()
                    }
                }
            }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::updateSearchQuery,
                label = {
                    Text("Search photos")
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        viewModel.onSearchQueryExecuted()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(12.dp))
            when (uiState.fetchingInitialPhotos) {
                true -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                false -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        state = lazyGridState,
                        modifier = Modifier.weight(1f)
                    ) {
                        items(uiState.photos) { photoUrl ->
                            AsyncImage(
                                model = Constants.IMAGE_LOADING_BASE_URL + photoUrl,
                                contentDescription = "Flickr image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}