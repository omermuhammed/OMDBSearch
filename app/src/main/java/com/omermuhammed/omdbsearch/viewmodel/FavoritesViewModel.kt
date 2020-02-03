package com.omermuhammed.omdbsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omermuhammed.omdbsearch.data.FavoritesEntity
import com.omermuhammed.omdbsearch.data.MovieDetailsEntity
import com.omermuhammed.omdbsearch.data.repository.OMDBSearchRepository
import com.omermuhammed.omdbsearch.network.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(
        val omdbSearchRepository: OMDBSearchRepository
) : ViewModel() {

    private val favoritesList: MutableLiveData<Resource<List<FavoritesEntity>>> = omdbSearchRepository.favoritesResult
    fun getFavoritesList(): LiveData<Resource<List<FavoritesEntity>>> = favoritesList

    fun fetchFavorites() {
        viewModelScope.launch {
            omdbSearchRepository.fetchFavorites()
        }
    }
}