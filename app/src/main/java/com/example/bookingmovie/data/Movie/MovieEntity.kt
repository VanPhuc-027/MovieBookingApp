package com.example.bookingmovie.data.Movie
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true) val movie_id: Int = 0,
    val title: String,
    val year: Int
)

