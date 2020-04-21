package com.tin.popularmovies.di.modules

import android.app.Application
import androidx.room.Room
import com.tin.popularmovies.room.AppDatabase
import com.tin.popularmovies.room.AppDatabase.Companion.DATABASE_NAME
import com.tin.popularmovies.room.MovieDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideAppDb(app: Application): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase): MovieDao {
        return db.getMovieDao()
    }
}