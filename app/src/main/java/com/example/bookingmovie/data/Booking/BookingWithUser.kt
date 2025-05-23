package com.example.bookingmovie.data.Booking

import androidx.room.Embedded
import androidx.room.Relation
import com.example.bookingmovie.data.User.UserEntity


data class BookingWithUser(
    @Embedded val booking: BookingEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "user_id"
    )
    val user: UserEntity
)
