package com.tin.popularmovies.ui.home

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.tin.popularmovies.Const
import com.tin.popularmovies.api.models.Movie

data class HomeViewState(
    val movies: List<Movie> = emptyList(),
    val isShowingSaved: Boolean = false,
    val isLoading: Boolean = true,
    val isNetworkError: Boolean = false,
    val isShowNetwork: Boolean = false,
    val isSigningOut: Boolean = false,
    val isUserLoggedIn: Boolean = false
)

