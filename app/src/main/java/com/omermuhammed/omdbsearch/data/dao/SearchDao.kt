package com.omermuhammed.omdbsearch.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.omermuhammed.omdbsearch.data.SearchEntity

@Dao
interface SearchDao {
    @Query("SELECT * from search ORDER BY Title DESC")
    suspend fun getAllSearchResults(): List<SearchEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchResult(search: SearchEntity)

    @Query("DELETE FROM search")
    suspend fun nukeTable()

}