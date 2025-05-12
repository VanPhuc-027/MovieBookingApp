package com.example.bookingmovie.data.Movie

import androidx.room.Embedded
import androidx.room.Relation
import com.example.bookingmovie.data.Genre.GenreEntity

data class MovieWithGenre(
    @Embedded val movie: MovieEntity,

    @Relation(
        parentColumn = "genre_id",
        entityColumn = "genre_id" ,
        entity = GenreEntity::class
    )
    val genre: List<GenreEntity>

)
