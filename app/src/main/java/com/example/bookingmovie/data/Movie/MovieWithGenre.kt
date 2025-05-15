package com.example.bookingmovie.data.Movie

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import com.example.bookingmovie.data.Genre.GenreEntity
import com.example.bookingmovie.data.MovieGenreCrossRef.MovieGenreCrossRefEntity

data class MovieWithGenre(
    @Embedded val movie: MovieEntity,

    @Relation(
        parentColumn = "movie_id",  // Khóa chính trong MovieEntity
        entityColumn = "genre_id",  // Khóa chính trong GenreEntity
        associateBy = Junction(
            value = MovieGenreCrossRefEntity::class,
            parentColumn = "movieId", // trong bảng trung gian
            entityColumn = "genreId"  // trong bảng trung gian
        )
    )
    val genre: List<GenreEntity>

)
