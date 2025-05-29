package com.example.bookingmovie.MovieUI.Movie

import com.example.bookingmovie.data.Genre.GenreEntity
import com.example.bookingmovie.data.Movie.MovieWithGenre

fun MovieWithGenre.toUIModel(): MovieUIModel {
    return MovieUIModel(
        movie_id = movie.movie_id,
        movie_name = movie.movie_name,
        description = movie.description,
        price = movie.price,
        banner = movie.banner,
        video = movie.video,
        year = movie.year,
        genres = genre.map { it.toUIModel() },
        releaseDate = movie.releaseDate
    )
}
