package com.tin.popularmovies.api.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.tin.popularmovies.api.TheMovieDbRepo
import com.tin.popularmovies.api.models.Movie
import com.tin.popularmovies.ui.home.HomeViewState
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Data Source is an incremental data loader for page-keyed content. It behaves like a repository
 */
class MovieDataSource @Inject constructor(
    private val movieDbRepo: TheMovieDbRepo,
    private val compositeDisposable: CompositeDisposable,
    private val viewState: MutableLiveData<HomeViewState>
) : PageKeyedDataSource<Int, Movie>() {

    private var retryCompletable: Completable? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {

        viewState.postValue(HomeViewState(isShowingSaved = false, isLoading = true))
        compositeDisposable.add(movieDbRepo.getTopRatedMovies(FIRST_PAGE).subscribe(
            {
                it?.let {
                    callback.onResult(it, null, FIRST_PAGE + INCREMENT)
                    viewState.postValue(
                        HomeViewState(
                            isShowingSaved = false,
                            isLoading = false,
                            isNetworkError = false
                        )
                    )
                }
            }, {
                setRetry(Action { loadInitial(params, callback) })

                viewState.postValue(
                    HomeViewState(
                        isNetworkError = true,
                        isLoading = false
                    )
                )
            }
        ))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

        // TODO: display a footer in the recyclerview with a loading icon
        compositeDisposable.add(movieDbRepo.getTopRatedMovies(params.key).subscribe(
            {
                val key =
                    it?.let { callback.onResult(it, params.key + INCREMENT) }
            }, {
                // TODO: display a footer in the recyclerView with a retry button
            }
        ))
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {}

    fun retryGetMovies() {
        retryCompletable?.let {
            compositeDisposable.add(
                it
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action != null) Completable.fromAction(action) else null
    }

    companion object {
        const val PAGE_SIZE = 20
        const val FIRST_PAGE = 1
        const val INCREMENT = 1
    }
}
