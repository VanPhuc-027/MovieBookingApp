package com.example.bookingmovie.data.Booking

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow


@Dao
interface BookingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(bookingEntity: BookingEntity)

    @Transaction
    @Query("SELECT * FROM Booking")
    fun getAllBookingsWithUsers(): Flow<List<BookingWithUser>>

}