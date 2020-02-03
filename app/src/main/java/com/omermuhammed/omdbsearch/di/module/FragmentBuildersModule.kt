package com.omermuhammed.omdbsearch.di.module

import com.omermuhammed.omdbsearch.ui.FavoritesFragment
import com.omermuhammed.omdbsearch.ui.HomeFragment
import com.omermuhammed.omdbsearch.ui.MovieDetailsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


// All fragments intended to use Dagger2 @Inject should be listed here
@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeMovieDetailsFragment(): MovieDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeFavoritesFragment(): FavoritesFragment
}