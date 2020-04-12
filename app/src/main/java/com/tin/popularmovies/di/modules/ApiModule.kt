package com.tin.popularmovies.di.modules

import android.app.Application
import com.tin.popularmovies.api.TheMovieDbApi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule {

    private val cacheSizeBytes = 1024 * 1024 * 5

    @Provides
    @Reusable
    fun providesRetrofit(cache: Cache, okHttpClient: OkHttpClient.Builder): TheMovieDbApi =
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .client(okHttpClient
                .cache(cache)
                .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(TheMovieDbApi::class.java)

    @Provides
    @Reusable
    fun providesOkHttpClient(): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

    @Provides
    @Reusable
    fun provideCache(application: Application): Cache = Cache(application.cacheDir, cacheSizeBytes.toLong())

}