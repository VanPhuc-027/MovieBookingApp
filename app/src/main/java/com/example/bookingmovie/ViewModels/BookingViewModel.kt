package com.example.bookingmovie.ViewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingmovie.data.Booking.BookingDao
import com.example.bookingmovie.data.Booking.BookingEntity
import com.example.bookingmovie.data.Booking.BookingWithMovie
import com.example.bookingmovie.data.Booking.BookingWithMovieAndUser
import com.example.bookingmovie.data.Seat.SeatDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BookingViewModel(
    private val bookingDao: BookingDao,
    private val seatDao: SeatDao
) : ViewModel() {
    val userBookings = mutableStateListOf<BookingEntity>()

    fun loadBookingsForUser(userId: Int) {
        viewModelScope.launch {
            bookingDao.getBookingsByUserId(userId).collectLatest { bookings ->
                userBookings.clear()
                userBookings.addAll(bookings)
            }
        }
    }
    fun getUserBookingWithMovie(userId: Int): Flow<List<BookingWithMovie>> {
        return bookingDao.getBookingsWithMovieByUserId(userId)
    }

    fun getAllBookingsWithMovieAndUser(): Flow<List<BookingWithMovieAndUser>> {
        return bookingDao.getAllBookingsWithMovieAndUser()
    }

    fun confirmBooking(
        userId: Int,
        showDate: String,
        showTime: String,
        selectedSeats: List<String>,
        selectedFood: Map<String, Int>,
        paymentMethod: String,
        totalPrice: Double,
        bookingTime: String,
        status: String = "Còn hạn",
        roomId: Int,
        showtimeId: Int,
        qrCodeContent : String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val booking = BookingEntity(
                userId = userId,
                showDate = showDate,
                showTime = showTime,
                numberOfTickets = selectedSeats.size,
                selectedSeats = selectedSeats.joinToString(","),
                selectedFood = selectedFood.entries.joinToString { "${it.key}:${it.value}" },
                paymentMethod = paymentMethod,
                totalPrice = totalPrice,
                bookingTime = bookingTime,
                status = status,
                roomId = roomId,
                showtimeId = showtimeId,
                qrCodeContent = qrCodeContent
            )
            bookingDao.insertBooking(booking)

            selectedSeats.forEach { seatNumber ->
                seatDao.updateSeatStatus(
                    seatNumber = seatNumber,
                    roomId = roomId,
                    showtimeId = showtimeId,
                    isBooked = true
                )
            }
            onSuccess()
        }
    }
}

