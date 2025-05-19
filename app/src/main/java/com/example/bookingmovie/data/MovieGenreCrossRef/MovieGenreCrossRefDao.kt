package com.example.bookingmovie.data.MovieGenreCrossRef

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface MovieGenreCrossRefDao {

    @Insert
    suspend fun insertCrossRef(crossRef: MovieGenreCrossRefEntity)

    @Query("DELETE FROM MovieGenreCrossRefEntity WHERE movieId = :movieId")
    suspend fun deleteCrossRefsByMovieId(movieId: Long)


}