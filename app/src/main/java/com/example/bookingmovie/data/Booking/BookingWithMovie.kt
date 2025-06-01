package com.example.bookingmovie.data.Booking

data class BookingWithMovie(
    val bookingId: Int,
    val userId: Int,
    val movieName: String,
    val showDate: String,
    val showTime: String,
    val bookingTime: String,
    val totalPrice: Double,
    val qrCodeContent: String,
    val selectedSeats: String,
    val selectedFood: String,
    val paymentMethod: String,
    val roomNumber: String,
    val status: String
)