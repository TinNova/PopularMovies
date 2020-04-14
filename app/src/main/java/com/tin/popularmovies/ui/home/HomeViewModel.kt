package com.tin.popularmovies.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.tin.popularmovies.DisposingViewModel
import com.tin.popularmovies.api.TheMovieDbRepo
import com.tin.popularmovies.api.models.Movie
import com.tin.popularmovies.api.paging.MovieDataSource
import com.tin.popularmovies.api.paging.MovieDataSourceFactory
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val theMovieDbRepo: TheMovieDbRepo
) : DisposingViewModel() {

    val viewState = MutableLiveData<HomeViewState>()
    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    private lateinit var liveDataSource: LiveData<MovieDataSource>

    fun onViewLoaded() {
        getTopRatedMovies()
    }

    private fun getTopRatedMovies() {
        val itemDataSourceFactory = MovieDataSourceFactory(theMovieDbRepo, compositeDisposable) // also send disposable
        liveDataSource = itemDataSourceFactory.movieLiveDataSource

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(MovieDataSource.PAGE_SIZE)
            .build()

        moviePagedList = LivePagedListBuilder(itemDataSourceFactory, config)
            .build()

    }

//    private fun getTopRatedMovies() {
//        add(theMovieDbRepo.getTopRatedMovies().subscribe(
//            {
//                viewState.value = HomeViewState(
//                    movies = it,
//                    isPresenting = true,
//                    isLoading = false,
//                    isNetworkError = false
//                )
//            }, {
//                viewState.value = HomeViewState(
//                    isPresenting = false,
//                    isLoading = false,
//                    isNetworkError = true
//                )
//            }
//        ))
//    }
}
