package com.omermuhammed.omdbsearch.data

import androidx.room.TypeConverter
import java.util.*

// Type converters to allow Room to reference complex data types, like Dates
// Not using it right now, but leaving here to show how to use them
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}