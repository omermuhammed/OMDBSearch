package com.omermuhammed.omdbsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omermuhammed.omdbsearch.data.MovieDetailsEntity
import com.omermuhammed.omdbsearch.data.repository.OMDBSearchRepository
import com.omermuhammed.omdbsearch.network.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(
        val omdbSearchRepository: OMDBSearchRepository
) : ViewModel() {

    private val movieDetails: MutableLiveData<Resource<MovieDetailsEntity>> = omdbSearchRepository.movieDetailsResult
    fun getMovieDetails(): LiveData<Resource<MovieDetailsEntity>> = movieDetails

    private val favoritesItemSaved: MutableLiveData<Boolean> = omdbSearchRepository.saveToFavoritesResult
    fun itemSavedToFavorites() : LiveData<Boolean> = favoritesItemSaved


    private val favoritesItemRemoved: MutableLiveData<Boolean> = omdbSearchRepository.removeFromFavoritesResult
    fun itemItemRemovedFavorites() : LiveData<Boolean> = favoritesItemRemoved

    fun fetchMovieDetails(imdbID: String) {
        viewModelScope.launch {
            omdbSearchRepository.fetchMovieDetails(imdbID)
        }
    }

    fun saveToFavorites(id: String, movieTitle: String, url: String) {
        viewModelScope.launch {
            omdbSearchRepository.saveToFavorites(id, movieTitle, url)
        }
    }

    fun removeFromFavorites(id: String) {
        viewModelScope.launch {
            omdbSearchRepository.removeFromFavorites(id)
        }
    }
}