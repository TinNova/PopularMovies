package com.tin.popularmovies

import com.tin.popularmovies.di.AppComponent
import com.tin.popularmovies.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App : DaggerApplication() {

    override fun applicationInjector():
            AndroidInjector<out DaggerApplication> {

        //Build app component
        val appComponent: AppComponent = DaggerAppComponent.builder()
            .application(this)
            .build()

        //inject application instance
        appComponent.inject(this)
        return appComponent
    }

}