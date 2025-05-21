package com.example.bookingmovie.data.Room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Room")
data class RoomEntity(
    @PrimaryKey(autoGenerate = true)
    val room_id: Int = 0,
    val Room_Number: String,
)