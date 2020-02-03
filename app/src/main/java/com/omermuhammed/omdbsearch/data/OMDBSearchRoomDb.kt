package com.omermuhammed.omdbsearch.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.omermuhammed.omdbsearch.data.dao.FavoritesDao
import com.omermuhammed.omdbsearch.data.dao.MovieDetailsDao
import com.omermuhammed.omdbsearch.data.dao.SearchDao

@Database(entities = [SearchEntity::class, MovieDetailsEntity::class, FavoritesEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class OMDBSearchRoomDb : RoomDatabase() {

    abstract fun searchDao(): SearchDao

    abstract fun movieDetailsDao(): MovieDetailsDao

    abstract fun favoritesDao(): FavoritesDao
}