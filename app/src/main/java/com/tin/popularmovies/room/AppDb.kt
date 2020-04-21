package com.tin.popularmovies.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MovieSql::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

    companion object {
        const val DATABASE_NAME: String = "app_db"
    }
}