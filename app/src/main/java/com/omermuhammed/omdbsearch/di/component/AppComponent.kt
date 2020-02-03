package com.omermuhammed.omdbsearch.di.component

import android.app.Application
import com.omermuhammed.omdbsearch.MainApp
import com.omermuhammed.omdbsearch.di.module.AppModule
import com.omermuhammed.omdbsearch.di.module.MainActivityModule
import com.omermuhammed.omdbsearch.di.module.OMDBSearchRepositoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    OMDBSearchRepositoryModule::class,
    MainActivityModule::class])

interface AppComponent {
    @Component.Builder
    interface Builder {

        // provide Application instance into DI
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    // this is needed because MainApp has @Inject
    fun inject(mainApp: MainApp)
}