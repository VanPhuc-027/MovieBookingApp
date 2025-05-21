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
        booking: BookingEntity,
        roomId: Int,
        selectedSeats: List<String>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            // 1. Thêm booking vào DB
            bookingDao.insertBooking(booking)

            // 2. Cập nhật trạng thái các ghế đã chọn
            selectedSeats.forEach { seatNumber ->
                seatDao.updateSeatStatus(seatNumber, roomId, isBooked = true)
            }

            // 3. Gọi callback báo thành công
            onSuccess()
        }
    }
}
