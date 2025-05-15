package com.example.bookingmovie.data.MovieGenreCrossRef

import androidx.room.Entity

@Entity(primaryKeys = ["movieId", "genreId"])
data class MovieGenreCrossRefEntity (
    val movieId: Int,
    val genreId: Int
)