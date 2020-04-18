package com.tin.popularmovies.di.modules

import com.tin.popularmovies.ui.LoginActivity
import com.tin.popularmovies.ui.detail.DetailActivity
import com.tin.popularmovies.ui.home.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun bindHomeActivity(): HomeActivity

    @ContributesAndroidInjector
    abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun bindDetailActivity(): DetailActivity

}