package com.tin.popularmovies.api

import com.tin.popularmovies.api.models.Movie
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface TheMovieDbApi {

    @GET("3/movie/top_rated?language=en-UK&page=1")
    fun getTopRatedMovies(@Query("api_key") apiKey: String): Single<Movie>
}