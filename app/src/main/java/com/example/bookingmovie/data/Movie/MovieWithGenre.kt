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
        parentColumn = "movie_id",
        entityColumn = "genre_id",
        associateBy = Junction(
            value = MovieGenreCrossRefEntity::class,
            parentColumn = "movieId",
            entityColumn = "genreId"
        )
    )
    val genre: List<GenreEntity>

)
