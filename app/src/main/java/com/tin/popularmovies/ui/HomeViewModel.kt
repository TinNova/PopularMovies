package com.tin.popularmovies.ui

import androidx.lifecycle.MutableLiveData
import com.tin.popularmovies.DisposingViewModel
import com.tin.popularmovies.TheMovieDbRepo
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val theMovieDbRepo: TheMovieDbRepo
) : DisposingViewModel() {

    val viewState = MutableLiveData<HomeViewState>()

    fun onViewLoaded() {
        getTopRatedMovies()
    }

    private fun getTopRatedMovies() {
        add(theMovieDbRepo.getTopRateMovies().subscribe(
            {
                viewState.value = HomeViewState(
                    movies = it,
                    isPresenting = true,
                    isLoading = false,
                    isNetworkError = false
                )
            }, {
                viewState.value = HomeViewState(
                    isPresenting = false,
                    isLoading = false,
                    isNetworkError = true
                )
            }
        ))
    }
}
