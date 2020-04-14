package com.tin.popularmovies.api.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.tin.popularmovies.api.TheMovieDbRepo
import com.tin.popularmovies.api.models.Movie
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Data Source is an incremental data loader for page-keyed content. It behaves like a repository
 */
class MovieDataSource @Inject constructor(
    private val movieDbRepo: TheMovieDbRepo,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Movie>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {

        compositeDisposable.add(movieDbRepo.getTopRatedMovies(FIRST_PAGE).subscribe(
            {
                it?.let { callback.onResult(it, null, FIRST_PAGE + INCREMENT) }
            }, {}
        ))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

        compositeDisposable.add(movieDbRepo.getTopRatedMovies(params.key).subscribe(
            {
                val key =
                    it?.let {
                        callback.onResult(it, params.key + INCREMENT)
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
