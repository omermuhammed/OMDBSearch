package com.omermuhammed.omdbsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omermuhammed.omdbsearch.data.SearchEntity
import com.omermuhammed.omdbsearch.data.repository.OMDBSearchRepository
import com.omermuhammed.omdbsearch.network.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

// Following MVVM architecture and ensuring data management through a view model
// Note that the view model only exposes LiveData

// Use @AssistedInject to pass in variable (https://proandroiddev.com/dagger-assisted-injection-2002885b3cba)
class HomeViewModel @Inject constructor(
    val omdbSearchRepository: OMDBSearchRepository
) : ViewModel() {

    private val totalPages: MutableLiveData<Int> = omdbSearchRepository.totalResults
    fun getTotalNumOfPages(): LiveData<Int> = totalPages

    private val searchResultsList: MutableLiveData<Resource<List<SearchEntity>>> = omdbSearchRepository.searchResults
    fun getSearchResults(): LiveData<Resource<List<SearchEntity>>> = searchResultsList

    fun fetchSearchResults(pageNumber: Int, searchTerm: String) {
        viewModelScope.launch {
            omdbSearchRepository.fetchSearchResults(pageNumber, searchTerm)
        }
    }
}