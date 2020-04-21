package com.tin.popularmovies.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tin.popularmovies.api.models.Movie

@Entity
data class MovieSql(
    @PrimaryKey val id: Int = 0,
    val title: String = "",
    val vote_average: Double = 0.0,
    val release_date: String = "",
    val overview: String = "",
    val poster_path: String = "",
    val backdrop_path: String = ""
)

fun Movie.mapToMovieSql() =
    MovieSql(
        id = id,
        title = title,
        vote_average = vote_average,
        release_date = release_date,
        overview = overview,
        poster_path = poster_path,
        backdrop_path = backdrop_path
    )