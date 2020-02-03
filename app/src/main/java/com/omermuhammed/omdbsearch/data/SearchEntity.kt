package com.omermuhammed.omdbsearch.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Using columinfo to make it easy to rename in case the schema changes
@Entity(tableName = "search")
data class SearchEntity(
        @PrimaryKey @ColumnInfo(name = "imdbID") var imdbID: String,
        @ColumnInfo(name = "Title") var Title: String = "",
        @ColumnInfo(name = "Year") var Year: String = "",
        @ColumnInfo(name = "Type") var Type: String = "",
        @ColumnInfo(name = "Poster") var Poster: String = ""
)
