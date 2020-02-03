package com.omermuhammed.omdbsearch.network

import com.omermuhammed.omdbsearch.data.MovieDetailsEntity
import com.omermuhammed.omdbsearch.data.SearchEntity
import com.squareup.moshi.Json

data class SearchResults(
        @Json(name = "Search")
        val Search: List<Search>,
        @Json(name = "totalResults")
        val totalResults: Int,
        @Json(name = "Response")
        val Response: String
)

data class Search(
        @Json(name = "Title")
        val Title: String,
        @Json(name = "Year")
        val Year: String,
        @Json(name = "imdbID")
        val imdbID: String,
        @Json(name = "Type")
        val Type: String,
        @Json(name = "Poster")
        val Poster: String
)

fun Search.toSearchEntity(): SearchEntity = SearchEntity(
        imdbID = imdbID,
        Title = Title,
        Year = Year,
        Type = Type,
        Poster = Poster
)

data class MovieDetails(
        @Json(name = "Title")
        val Title: String,
        @Json(name = "Year")
        val Year: String,
        @Json(name = "Rated")
        val Rated: String,
        @Json(name = "Director")
        val Director: String,
        @Json(name = "Actors")
        val Actors: String,
        @Json(name = "Plot")
        val Plot: String,
        @Json(name = "imdbRating")
        val imdbRating: String,
        @Json(name = "Metascore")
        val Metascore: String,
        @Json(name = "Type")
        val Type: String,
        @Json(name = "Poster")
        val Poster: String
)

fun MovieDetails.toMovieDetailsEntity(): MovieDetailsEntity = MovieDetailsEntity(
        Title = Title,
        Year = Year,
        Rated = Rated,
        Director = Director,
        Actors = Actors,
        Plot = Plot,
        imdbRating = imdbRating,
        Metascore = Metascore,
        Type = Type,
        Poster = Poster
)