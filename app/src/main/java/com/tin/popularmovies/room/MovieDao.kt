package com.tin.popularmovies.room

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovie(movie: MovieSql): Completable

    @Query("SELECT * FROM moviesql")
    fun getAllMovies(): Single<List<MovieSql>>

    @Query("SELECT * FROM moviesql WHERE id = :id")
    fun getMovieById(id: Int): Single<MovieSql>

    @Delete
    fun deleteMovie(movie: MovieSql): Completable
}