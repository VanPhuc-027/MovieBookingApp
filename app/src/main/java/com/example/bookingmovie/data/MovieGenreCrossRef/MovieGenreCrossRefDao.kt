package com.example.bookingmovie.data.MovieGenreCrossRef

import androidx.room.Dao
import androidx.room.Insert


@Dao
interface MovieGenreCrossRefDao {

    @Insert
    suspend fun insertCrossRef(crossRef: MovieGenreCrossRefEntity)

}