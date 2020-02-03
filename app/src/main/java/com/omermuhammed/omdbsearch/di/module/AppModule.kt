package com.omermuhammed.omdbsearch.di.module

import android.app.Application
import androidx.room.Room
import com.omermuhammed.omdbsearch.data.OMDBSearchRoomDb
import com.omermuhammed.omdbsearch.data.dao.FavoritesDao
import com.omermuhammed.omdbsearch.data.dao.MovieDetailsDao
import com.omermuhammed.omdbsearch.data.dao.SearchDao
import com.omermuhammed.omdbsearch.network.ApiInterface
import com.omermuhammed.omdbsearch.utils.*
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

// Top level module for stuff used everywhere in app
@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideDatabase(app: Application): OMDBSearchRoomDb {
        return Room.databaseBuilder(app,
            OMDBSearchRoomDb::class.java, DB_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun provideFavoritesDao(db: OMDBSearchRoomDb): FavoritesDao {
        return db.favoritesDao()
    }

    @Singleton
    @Provides
    fun provideMovieDetailsDao(db: OMDBSearchRoomDb): MovieDetailsDao {
        return db.movieDetailsDao()
    }

    @Singleton
    @Provides
    fun provideSearchDao(db: OMDBSearchRoomDb): SearchDao {
        return db.searchDao()
    }

    @Provides
    @Singleton
    internal fun provideMoshiConverterFactory(): MoshiConverterFactory {
        return MoshiConverterFactory.create()
    }

    @Provides
    @Singleton
    internal fun provideOkhttpClient(): OkHttpClient {

        val apiKeyInterceptor = Interceptor { chain ->
            val url: HttpUrl = chain.request()
                    .url.newBuilder()
                    .addQueryParameter(API_KEY_QUERY_STRING, Keys.apiKey())
                    .build()

            val request: Request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

            chain.proceed(request)
        }

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(NoConnectionInterceptor())
        httpClient.addInterceptor(apiKeyInterceptor)
        return httpClient.build()

    }

    @Provides
    @Singleton
    internal fun provideRetrofit(moshiConverterFactory: MoshiConverterFactory, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().client(okHttpClient)
            .baseUrl(OMDBSEARCH_URL)
            .addConverterFactory(moshiConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    internal fun provideOMDBSearchApiService(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }
}