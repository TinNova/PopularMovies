package com.tin.popularmovies

import com.tin.popularmovies.api.TheMovieDbApi
import com.tin.popularmovies.api.models.Movie
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TheMovieDbRepo @Inject constructor(private val theMovieDbApi: TheMovieDbApi) {

    fun getTopRateMovies(): Single<List<Movie>> =
        theMovieDbApi.getTopRatedMovies(BuildConfig.MOVIE_DATA_BASE_API)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.results }
}
