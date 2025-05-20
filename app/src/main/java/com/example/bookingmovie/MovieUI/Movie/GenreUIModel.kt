package com.example.bookingmovie.MovieUI.Movie

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenreUIModel(
    val genre_id: Int,
    val genre_name: String
) : Parcelable
