package com.tin.popularmovies.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.google.firebase.firestore.ktx.toObjects
import com.tin.popularmovies.DisposingViewModel
import com.tin.popularmovies.api.FireCloud
import com.tin.popularmovies.api.TheMovieDbRepo
import com.tin.popularmovies.api.models.Movie
import com.tin.popularmovies.api.paging.MovieDataSource
import com.tin.popularmovies.api.paging.MovieDataSourceFactory
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val theMovieDbRepo: TheMovieDbRepo,
    private val fireCloud: FireCloud
) : DisposingViewModel() {

    val viewState = MutableLiveData<HomeViewState>()
    lateinit var pagedMovieState: LiveData<PagedList<Movie>>
    private lateinit var movieDataSourceLiveData: LiveData<MovieDataSource>
    private var isLoggedIn = false

    fun onViewLoaded() {
        getTopRatedMovies()

        isLoggedIn = fireCloud.isUserLoggedIn()
        viewState.value = HomeViewState(isUserLoggedIn = isLoggedIn)
    }

    private fun getTopRatedMovies() {
        val itemDataSourceFactory =
            MovieDataSourceFactory(theMovieDbRepo, compositeDisposable, viewState)
        movieDataSourceLiveData = itemDataSourceFactory.movieDataSourceLiveData

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(MovieDataSource.PAGE_SIZE)
            .build()

        pagedMovieState = LivePagedListBuilder(itemDataSourceFactory, config).build()

    }

    fun retryGetMovies() {
        movieDataSourceLiveData.value?.retryGetMovies()
    }

    fun onFavouriteIconClicked(isShowingSaved: Boolean) {

        if (isShowingSaved) {
            showMoviesFromNetwork()
        } else {
            if (isLoggedIn) showMoviesFromCloud() else showMoviesFromRoom()
        }
    }

    fun onLogoutIconClicked() {
        fireCloud.auth.signOut()
        viewState.value = HomeViewState(isSigningOut = true)
    }

    private fun showMoviesFromNetwork() {
        viewState.value = HomeViewState(
            movies = emptyList(),
            isShowingSaved = false,
            isLoading = false,
            isNetworkError = false
        )

    }

    private fun showMoviesFromCloud() {

        fireCloud.moviesCollection
            .get()
            .addOnSuccessListener { documentSnapshot ->

                val movies: List<Movie> = documentSnapshot.toObjects()

                viewState.value = HomeViewState(
                    movies = movies,
                    isShowingSaved = true,
                    isLoading = false,
                    isNetworkError = false
                )
                Log.d("Movie", "Document Retrieved: ${movies.size}")

            }.addOnFailureListener {

                Log.d("Movie", "Document Failed To Retrieve")

            }
    }

    private fun showMoviesFromRoom() {
        add(theMovieDbRepo.getAllMovies().subscribe({
            viewState.value = HomeViewState(
                movies = it,
                isShowingSaved = true,
                isLoading = false,
                isNetworkError = false
            )
        }, {

        }))

    }
}
