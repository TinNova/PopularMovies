package com.tin.popularmovies.api.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.tin.popularmovies.api.TheMovieDbRepo
import com.tin.popularmovies.api.models.Movie
import com.tin.popularmovies.ui.home.HomeViewState
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Data Source is an incremental data loader for page-keyed content. It behaves like a repository
 */
class MovieDataSource @Inject constructor(
    private val movieDbRepo: TheMovieDbRepo,
    private val compositeDisposable: CompositeDisposable,
    private val viewState: MutableLiveData<HomeViewState>
) : PageKeyedDataSource<Int, Movie>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {

//        viewState.value = HomeViewState(isPresenting = false, isLoading = true)
        compositeDisposable.add(movieDbRepo.getTopRatedMovies(FIRST_PAGE).subscribe(
            {
                it?.let {
                    callback.onResult(it, null, FIRST_PAGE + INCREMENT)
//                    viewState.value = HomeViewState(isPresenting = true, isLoading = false)
                }
            }, {
            }
        ))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

//        viewState.value = HomeViewState(isLoadExtraPage = true)
        compositeDisposable.add(movieDbRepo.getTopRatedMovies(params.key).subscribe(
            {
                val key =
                    it?.let {
                        callback.onResult(it, params.key + INCREMENT)
//                        viewState.value = HomeViewState(isLoadExtraPage = false)
                    }
            }, {}
        ))
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {}

    companion object {
        const val PAGE_SIZE = 20
        const val FIRST_PAGE = 1
        const val INCREMENT = 1
    }

}
