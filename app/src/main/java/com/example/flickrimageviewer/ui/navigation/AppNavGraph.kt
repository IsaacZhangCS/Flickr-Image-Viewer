package com.example.flickrimageviewer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flickrimageviewer.ui.screens.MainSearchScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            MainSearchScreen(modifier = modifier)
        }
    }
}