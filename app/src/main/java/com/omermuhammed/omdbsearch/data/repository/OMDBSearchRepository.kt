package com.omermuhammed.omdbsearch.data.repository

import androidx.lifecycle.MutableLiveData
import com.omermuhammed.omdbsearch.data.FavoritesEntity
import com.omermuhammed.omdbsearch.data.MovieDetailsEntity
import com.omermuhammed.omdbsearch.data.SearchEntity
import com.omermuhammed.omdbsearch.data.dao.FavoritesDao
import com.omermuhammed.omdbsearch.data.dao.MovieDetailsDao
import com.omermuhammed.omdbsearch.data.dao.SearchDao
import com.omermuhammed.omdbsearch.network.*
import retrofit2.Response

class OMDBSearchRepository(
        private val searchDao: SearchDao,
        private val movieDetailsDao: MovieDetailsDao,
        private val favoritesDao: FavoritesDao,
        private val apiClient: ApiInterface
) {

    val totalResults: MutableLiveData<Int> = MutableLiveData()
    val searchResults: MutableLiveData<Resource<List<SearchEntity>>> = MutableLiveData()

    val movieDetailsResult: MutableLiveData<Resource<MovieDetailsEntity>> = MutableLiveData()

    val favoritesResult: MutableLiveData<Resource<List<FavoritesEntity>>> = MutableLiveData()
    val saveToFavoritesResult: MutableLiveData<Boolean> = MutableLiveData()
    val removeFromFavoritesResult: MutableLiveData<Boolean> = MutableLiveData()

    suspend fun fetchSearchResults(pageNumber: Int, searchString: String) {
        var results: Resource<List<SearchEntity>>

        results = Resource.Loading()
        searchResults.postValue(results)

        try {
            val response: Response<SearchResults> = apiClient.getOMDBSearchResults(page = pageNumber, searchTerm = searchString)

            if (response.isSuccessful) {
                val searchResults: SearchResults = response.body()!!

                searchDao.nukeTable()

                if (!searchResults.Search.isNullOrEmpty()) {

                    val searchResultsList: List<Search> = searchResults.Search

                    var searchEntity: SearchEntity

                    searchResultsList.forEach {
                        searchEntity = it.toSearchEntity()
                        searchDao.insertSearchResult(searchEntity)
                    }

                    val totalResults: Int = searchResults.totalResults
                    this.totalResults.postValue(totalResults)

                    results = Resource.Success(searchDao.getAllSearchResults())
                    this.searchResults.postValue(results)
                } else {
                    results = Resource.Success(emptyList())
                    this.searchResults.postValue(results)

                    this.totalResults.postValue(0)
                }

            } else {
                results = Resource.Failure(
                        "Response code:" +
                                response.code() +
                                ", error message: " +
                                response.message())

                searchResults.postValue(results)
            }
        } catch (e: Throwable) {
            results = Resource.Failure(e.message!!)
            searchResults.postValue(results)
        }
    }

    suspend fun fetchMovieDetails(IMDBId: String) {
        var result: Resource<MovieDetailsEntity>

        result = Resource.Loading()
        movieDetailsResult.postValue(result)

        try {
            val response: Response<MovieDetails> = apiClient.getMovieDetails(id = IMDBId)

            if (response.isSuccessful) {
                val movieDetails: MovieDetails = response.body()!!

                val movieDetailsEntity: MovieDetailsEntity = movieDetails.toMovieDetailsEntity()
                movieDetailsDao.insertMovieDetails(movieDetailsEntity)

                result = Resource.Success(movieDetailsDao.getMovieDetails(movieDetailsEntity.Title))
                this.movieDetailsResult.postValue(result)

            } else {
                result = Resource.Failure(
                        "Response code:" +
                                response.code() +
                                ", error message: " +
                                response.message())

                movieDetailsResult.postValue(result)
            }
        } catch (e: Throwable) {
            result = Resource.Failure(e.message!!)
            movieDetailsResult.postValue(result)
        }
    }

    suspend fun fetchFavorites() {
        var results: Resource<List<FavoritesEntity>>

        results = Resource.Loading()
        favoritesResult.postValue(results)

        try {
            val favoritesFromDB: List<FavoritesEntity> = favoritesDao.getAllFavorites()

            if (!favoritesFromDB.isNullOrEmpty()) {

                results = Resource.Success(favoritesDao.getAllFavorites())
                this.favoritesResult.postValue(results)
            } else {
                results = Resource.Failure("No Favorites saved yet")
                favoritesResult.postValue(results)
            }
        } catch (e: Throwable) {
            results = Resource.Failure(e.message!!)
            favoritesResult.postValue(results)
        }
    }

    suspend fun saveToFavorites(id: String, movieTitle: String, url: String) {

        try {
            var favoritesEntity = FavoritesEntity(id, movieTitle, "movie", url)

            favoritesDao.insertFavoriteItem(favoritesEntity)
            this.saveToFavoritesResult.postValue(true)

        } catch (e: Throwable) {
            saveToFavoritesResult.postValue(false)
        }
    }

    suspend fun removeFromFavorites(id: String) {

        try {
            favoritesDao.deleteFavoriteItem(id)
            this.removeFromFavoritesResult.postValue(true)

        } catch (e: Throwable) {
            removeFromFavoritesResult.postValue(false)
        }
    }
}
