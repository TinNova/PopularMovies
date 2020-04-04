package com.tin.popularmovies.api.models

import android.os.Parcelable
import com.tin.popularmovies.Const.BASE_IMAGE_URL
import kotlinx.android.parcel.Parcelize


data class MoviesResult(
    val page: Int,
    val total_results: Int,
    val total_pages: Int,
    val results: List<Movie>
)

@Parcelize
data class Movie(
    val vote_count: Int = 0,
    val id: Int = 0,
    val video: Boolean = false,
    val vote_average: Double = 0.0,
    val title: String = "",
    val popularity: Double = 0.0,
    val poster_path: String = "",
    val original_language: String = "",
    val original_title: String = "",
    val genre_ids: List<Int> = emptyList(),
    val backdrop_path: String = "",
    val adult: Boolean = false,
    val overview: String = "",
    val release_date: String = ""
): Parcelable

fun Movie.returnCleanMovie() =
    Movie(
        id = id,
        title = title,
        vote_average = vote_average,
        release_date = release_date,
        overview = overview,
        poster_path = BASE_IMAGE_URL + poster_path,
        backdrop_path = BASE_IMAGE_URL + backdrop_path
    )
