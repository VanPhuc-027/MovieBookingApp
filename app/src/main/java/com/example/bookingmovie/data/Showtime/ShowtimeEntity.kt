package com.example.bookingmovie.data.Showtime

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "showtime")
data class ShowtimeEntity(
    @PrimaryKey(autoGenerate = true)
    val showtimeId: Int = 0,
    val roomId: Int,
    val movieId: Long,
    val showTime: String,
    val showDate :String
)
