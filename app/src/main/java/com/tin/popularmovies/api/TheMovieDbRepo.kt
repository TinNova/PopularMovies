package com.tin.popularmovies.api

import com.tin.popularmovies.BuildConfig
import com.tin.popularmovies.api.models.*
import com.tin.popularmovies.room.MovieDao
import com.tin.popularmovies.room.MovieSql
import com.tin.popularmovies.ui.detail.DetailData
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TheMovieDbRepo @Inject constructor(
    private val theMovieDbApi: TheMovieDbApi,
    private val movieDao: MovieDao
) {

    fun getTopRatedMovies(page: Int): Single<List<Movie>> =
        theMovieDbApi.getTopRatedMovies(
            BuildConfig.MOVIE_DATA_BASE_API,
            page
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flattenAsObservable { it.results }
            .map { it.returnCleanMovie() }
            .toList()

    private fun getTrailers(movieId: Int): Single<List<Trailer>> =
        theMovieDbApi.getTrailers(
            movieId,
            BuildConfig.MOVIE_DATA_BASE_API
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flattenAsObservable { it.trailers }
            .map { it.returnCleanTrailer() }
            .toList()

    private fun getCast(movieId: Int): Single<List<Cast>> =
        theMovieDbApi.getCast(
            movieId,
            BuildConfig.MOVIE_DATA_BASE_API
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flattenAsObservable { it.cast }
            .map { it.returnCleanCast() }
            .toList()


    private fun getDetail(movieId: Int): Single<Detail> =
        theMovieDbApi.getDetail(
            movieId,
            BuildConfig.MOVIE_DATA_BASE_API
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.returnCleanDetail() }

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

    fun insertMovie(movie: MovieSql): Completable = movieDao.insertMovie(movie)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun getMovieById(id: Int): Single<MovieSql> = movieDao.getMovieById(id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun deleteMovie(movieSql: MovieSql): Completable = movieDao.deleteMovie(movieSql)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

}
