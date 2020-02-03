package com.omermuhammed.omdbsearch.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("?type=movie")
    suspend fun getOMDBSearchResults(
        @Query("s") searchTerm: String,
        @Query("page") page: Int
    ): Response<SearchResults>

    @GET("?")
    suspend fun getMovieDetails(
            @Query("i") id: String
    ): Response<MovieDetails>

}