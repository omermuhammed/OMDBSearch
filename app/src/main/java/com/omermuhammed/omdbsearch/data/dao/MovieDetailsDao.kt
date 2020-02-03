package com.omermuhammed.omdbsearch.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.omermuhammed.omdbsearch.data.MovieDetailsEntity

@Dao
interface MovieDetailsDao {
    @Query("SELECT * from moviedetails WHERE Title LIKE :title")
    suspend fun getMovieDetails(title: String): MovieDetailsEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetails(movieDetail: MovieDetailsEntity)
}