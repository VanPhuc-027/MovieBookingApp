package com.example.bookingmovie.data.Seat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SeatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeats(seats: List<SeatEntity>)

    @Query("SELECT * FROM Seat WHERE roomId= :roomId")
    fun getSeatsByRoomId(roomId: Int): Flow<List<SeatEntity>>

    @Query("UPDATE Seat SET isBooked = :isBooked WHERE seatNumber = :seatNumber AND roomId = :roomId")
    suspend fun updateSeatStatus(seatNumber: String, roomId: Int, isBooked: Boolean)

    @Update
    suspend fun updateSeat(seat: SeatEntity)
}