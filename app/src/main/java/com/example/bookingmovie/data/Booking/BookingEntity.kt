package com.example.bookingmovie.data.Booking

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Booking")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val bookingId: Int = 0,
    val userId: Int,
    val showDate: String,
    val showTime: String,
    val numberOfTickets: Int,
    val selectedSeats: String, // "A1,B2,C3"
    val selectedFood: String,
    val paymentMethod: String,
    val totalPrice: Double,
    val bookingTime: String,
    val status: String, // "Còn hạn", "Hết hạn"
    //val qrCodeData: String = ""
)
