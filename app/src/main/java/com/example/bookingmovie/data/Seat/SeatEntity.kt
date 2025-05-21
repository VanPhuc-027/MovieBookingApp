package com.example.bookingmovie.data.Seat

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Seat")
data class SeatEntity (
    @PrimaryKey(autoGenerate = true) val Seat_id: Int =0,
    val seatNumber: String,
    val isBooked: Boolean,
    val row: String,
    val roomId: Int
)