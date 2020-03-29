package com.tin.popularmovies.api

import com.tin.popularmovies.api.models.Credit
import com.tin.popularmovies.api.models.Detail
import com.tin.popularmovies.api.models.MoviesResult
import com.tin.popularmovies.api.models.TrailersResult
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDbApi {

    @GET("3/movie/top_rated?language=en-UK&page=1")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String
    ): Single<MoviesResult>

    @GET("3/movie/{MOVIE_ID}/videos?language=en-UK")
    fun getTrailers(
        @Path("MOVIE_ID") movieId: Int,
        @Query("api_key") apiKey: String
    ): Single<TrailersResult>

    @GET("3/movie/{MOVIE_ID}/credits?language=en-UK")
    fun getCast(
        @Path("MOVIE_ID") movieId: Int,
        @Query("api_key") apiKey: String
    ): Single<Credit>

    @GET("3/movie/{MOVIE_ID}?language=en-UK")
    fun getDetail(
        @Path("MOVIE_ID") movieId: Int,
        @Query("api_key") apiKey: String
    ): Single<Detail>

}
