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

    @Query("SELECT * FROM booking WHERE userId = :userId")
    fun getBookingsByUserId(userId: Int): Flow<List<BookingEntity>>

    @Query("""
    SELECT 
        b.bookingId,
        b.userId,
        m.movie_name AS movieName,
        m.releaseDate AS showDate,
        s.showTime,
        b.bookingTime,
        b.totalPrice,
        b.qrCodeContent,
        b.selectedSeats,
        b.selectedFood,
        b.paymentMethod,
        r.room_number AS roomNumber
    FROM Booking b
    INNER JOIN Showtime s ON b.showtimeId = s.showtimeId
    INNER JOIN movies m ON s.movieId = m.movie_id
    INNER JOIN Room r ON s.roomId = r.room_id
    WHERE b.userId = :userId
    ORDER BY b.bookingTime DESC
""")
    fun getBookingsWithMovieByUserId(userId: Int): Flow<List<BookingWithMovie>>

    @Query("""
    SELECT 
        b.bookingId,
        b.userId,
        u.username AS userName,
        m.movie_name AS movieName,
        m.releaseDate AS showDate,
        s.showTime,
        b.bookingTime,
        b.totalPrice,
        b.qrCodeContent,
        b.selectedSeats,
        b.selectedFood,
        b.paymentMethod,
        r.room_number AS roomNumber
    FROM Booking b
    INNER JOIN Showtime s ON b.showtimeId = s.showtimeId
    INNER JOIN movies m ON s.movieId = m.movie_id
    INNER JOIN Room r ON s.roomId = r.room_id
    INNER JOIN users u ON b.userId = u.user_id
    ORDER BY b.bookingTime DESC
""")
    fun getAllBookingsWithMovieAndUser(): Flow<List<BookingWithMovieAndUser>>
}