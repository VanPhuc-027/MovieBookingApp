package com.example.bookingmovie.data.Showtime

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ShowtimeDao {
    @Query("SELECT * FROM Showtime WHERE movieId = :movieId")
    fun getShowtimesByMovieId( movieId: Long): Flow<List<ShowtimeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShowtime(showtime: ShowtimeEntity): Long

    @Query("SELECT * FROM Showtime")
    suspend fun getAllShowtimesOnce(): List<ShowtimeEntity>

}
