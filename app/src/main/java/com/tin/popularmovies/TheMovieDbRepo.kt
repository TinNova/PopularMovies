package com.tin.popularmovies

import com.tin.popularmovies.api.TheMovieDbApi
import com.tin.popularmovies.api.models.*
import com.tin.popularmovies.ui.detail.DetailData
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TheMovieDbRepo @Inject constructor(private val theMovieDbApi: TheMovieDbApi) {

    fun getTopRateMovies(): Single<List<Movie>> =
        theMovieDbApi.getTopRatedMovies(BuildConfig.MOVIE_DATA_BASE_API)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.results }

    private fun getTrailers(movieId: Int): Single<List<Trailer>> =
        theMovieDbApi.getTrailers(movieId, BuildConfig.MOVIE_DATA_BASE_API)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.trailers }

    private fun getCast(movieId: Int): Single<List<Cast>> =
        theMovieDbApi.getCast(movieId, BuildConfig.MOVIE_DATA_BASE_API)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flattenAsObservable { it.cast }
            .map { it.appendBaseImgUrl() }
            .toList()


    private fun getDetail(movieId: Int): Single<Detail> =
        theMovieDbApi.getDetail(movieId, BuildConfig.MOVIE_DATA_BASE_API)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getDetailData(movieId: Int): Single<DetailData> = Single.zip(
        getTrailers(movieId),
        getCast(movieId),
        getDetail(movieId),
        Function3<List<Trailer>, List<Cast>, Detail, DetailData> { trailer,
                                                                   cast,
                                                                   detail ->
            DetailData(
                detail = detail,
                trailers = trailer,
                cast = cast
            )
        }
    )
}
