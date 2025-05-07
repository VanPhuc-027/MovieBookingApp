package com.example.bookingmovie.data.Movie
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true) val movie_id: Int = 0,
    val movie_name: String,
    val genre_id : Int,
    val showtime_id : Int,
    val description :String,
    val price : Float,
    val banner : String?,
    val video : String?,
    val year: Int
)

