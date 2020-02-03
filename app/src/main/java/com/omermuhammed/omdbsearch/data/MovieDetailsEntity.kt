package com.omermuhammed.omdbsearch.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Using columinfo to make it easy to rename in case the schema changes
@Entity(tableName = "moviedetails")
data class MovieDetailsEntity(
        @PrimaryKey @ColumnInfo(name = "Title") var Title: String,
        @ColumnInfo(name = "Year") var Year: String = "",
        @ColumnInfo(name = "Rated") var Rated: String = "",
        @ColumnInfo(name = "Director") var Director: String = "",
        @ColumnInfo(name = "Actors") var Actors: String = "",
        @ColumnInfo(name = "Plot") var Plot: String = "",
        @ColumnInfo(name = "imdbRating") var imdbRating: String = "",
        @ColumnInfo(name = "Metascore") var Metascore: String = "",
        @ColumnInfo(name = "Type") var Type: String = "",
        @ColumnInfo(name = "Poster") var Poster: String = ""
)
