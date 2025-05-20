package com.example.bookingmovie.MovieUI.Movie

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieUIModel(
    val movie_id: Long = 0,
    val movie_name: String,
    val description:String,
    val price: Double,
    val banner: String?,
    val video: String?,
    val year: Int,
    val genres: List<GenreUIModel>
) : Parcelable

