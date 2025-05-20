package com.example.bookingmovie.MovieUI.Movie


import com.example.bookingmovie.MovieUI.Movie.MovieUIModel
import com.example.bookingmovie.data.Genre.GenreEntity
import com.example.bookingmovie.data.Movie.MovieEntity

fun MovieEntity.toUIModel(): MovieUIModel {
    return MovieUIModel(
        movie_id = this.movie_id,
        movie_name = this.movie_name,
        description = this.description,
        price = this.price,
        banner = this.banner,
        video = this.video,
        year = this.year,
        genres = emptyList()
    )
}
fun GenreEntity.toUIModel(): GenreUIModel {
    return GenreUIModel(
        genre_id = this.genre_id,
        genre_name = this.genre_name
    )
}