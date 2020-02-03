package com.omermuhammed.omdbsearch.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.omermuhammed.omdbsearch.data.FavoritesEntity

@Dao
interface FavoritesDao {
    @Query("SELECT * from favorites ORDER BY Title DESC")
    suspend fun getAllFavorites(): List<FavoritesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteItem(favoriteItem: FavoritesEntity)

    @Query("DELETE FROM favorites WHERE imdbID LIKE :id")
    suspend fun deleteFavoriteItem(id: String)

}