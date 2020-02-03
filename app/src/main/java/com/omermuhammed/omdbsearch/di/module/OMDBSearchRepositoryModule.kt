package com.omermuhammed.omdbsearch.di.module

import com.omermuhammed.omdbsearch.data.dao.FavoritesDao
import com.omermuhammed.omdbsearch.data.dao.MovieDetailsDao
import com.omermuhammed.omdbsearch.data.dao.SearchDao
import com.omermuhammed.omdbsearch.data.repository.OMDBSearchRepository
import com.omermuhammed.omdbsearch.network.ApiInterface
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class OMDBSearchRepositoryModule {

    @Provides
    @Singleton
    internal fun provideOMDBRepository(
            searchDao: SearchDao,
            movieDetailsDao: MovieDetailsDao,
            favoritesDao: FavoritesDao,
            apiInterface: ApiInterface
    ): OMDBSearchRepository {
        return OMDBSearchRepository(
                searchDao,
                movieDetailsDao,
                favoritesDao,
                apiInterface)
    }
}