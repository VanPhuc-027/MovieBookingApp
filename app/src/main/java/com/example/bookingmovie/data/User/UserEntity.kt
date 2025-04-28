package com.example.bookingmovie.data.User

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val user_id : Int = 0,
    val username : String,
    val gmail : String,
    val phone_number : Int,
    val password : String,
    val role : String
)