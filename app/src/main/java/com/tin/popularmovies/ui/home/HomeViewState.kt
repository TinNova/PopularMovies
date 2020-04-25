package com.tin.popularmovies.ui.home

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.tin.popularmovies.Const
import com.tin.popularmovies.api.models.Movie

data class HomeViewState(
    val movies: List<Movie> = emptyList(),
    val isPresenting: Boolean = false,
    val isLoading: Boolean = true,
    val isNetworkError: Boolean = false,
    val isShowingCloud: Boolean = false, // this isn't used
    val isShowingRoom: Boolean = false, // this isn't used
    val isShowNetwork: Boolean = false // this isn't used
)

