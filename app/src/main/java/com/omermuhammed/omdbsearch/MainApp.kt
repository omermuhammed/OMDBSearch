package com.omermuhammed.omdbsearch

import android.app.Application
import android.content.Context
import com.omermuhammed.omdbsearch.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class MainApp : Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext

        // We also do Dagger initialization here
        AppInjector.init(this)

        // Logging app crashes with Crashlytics only for release builds
        // Uncomment this when you add your own google-services.json file
//        if (!BuildConfig.DEBUG) {
//            Fabric.with(this, Crashlytics())
//            Timber.plant(CrashReportingTree())
//        } else {
//            Timber.plant(Timber.DebugTree())
//        }

        Timber.plant(Timber.DebugTree())
    }

    companion object {
        lateinit var appContext: Context
    }
}
