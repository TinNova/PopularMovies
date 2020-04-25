package com.tin.popularmovies.api.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.tin.popularmovies.api.TheMovieDbRepo
import com.tin.popularmovies.api.models.Movie
import com.tin.popularmovies.ui.home.HomeViewState
import io.reactivex.disposables.CompositeDisposable

/**
 * The Paging Library requires us to use a DataSource.Factory. It's just a factory for DataSources
 */
class MovieDataSourceFactory(
    private val movieDbRepo: TheMovieDbRepo,
    private val compositeDisposable: CompositeDisposable,
    private val viewState: MutableLiveData<HomeViewState>
) :
    DataSource.Factory<Int, Movie>() {

    // Is this where the MovieDataSource callBacks are sent?
    val movieLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(movieDbRepo, compositeDisposable, viewState)
        movieLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }

}
