package com.example.bookingmovie.data.Movie

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie :MovieEntity)

    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()
}