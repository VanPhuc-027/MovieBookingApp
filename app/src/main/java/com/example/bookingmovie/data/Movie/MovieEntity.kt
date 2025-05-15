package com.example.bookingmovie.data.Movie
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true) val movie_id: Long = 0,
    val movie_name: String,
    val description:String,
    val price: Double,
    val banner: String?,
    val video: String?,
    val year: Int
)

