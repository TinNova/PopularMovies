package com.tin.popularmovies.ui.home

import com.tin.popularmovies.api.models.Movie

data class HomeViewState(
    val movies: List<Movie> = emptyList(),
    val isPresenting: Boolean = false,
    val isLoading: Boolean = true,
    val isNetworkError: Boolean = false
)
