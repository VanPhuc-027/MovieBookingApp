package com.example.bookingmovie.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingmovie.data.Booking.BookingDao
import com.example.bookingmovie.data.Booking.BookingEntity
import com.example.bookingmovie.data.Seat.SeatDao
import kotlinx.coroutines.launch

class BookingViewModel(
    private val bookingDao: BookingDao,
    private val seatDao: SeatDao
) : ViewModel() {

    fun confirmBooking(
        userId: Int, // 👈 Thêm userId ở đây
        showDate: String,
        showTime: String,
        selectedSeats: List<String>,
        selectedFood: Map<String, Int>,
        paymentMethod: String,
        totalPrice: Double,
        bookingTime: String,
        status: String = "Còn hạn",
        roomId: Int,
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
                status = status
            )

            bookingDao.insertBooking(booking)

            selectedSeats.forEach { seatNumber ->
                seatDao.updateSeatStatus(seatNumber, roomId, isBooked = true)
            }

            onSuccess()
        }
    }
}

