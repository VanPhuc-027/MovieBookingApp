package com.example.bookingmovie

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.bookingmovie.ui.theme.BookingMovieTheme


@Composable
fun MovieList(navController: NavController){
    Box (
        modifier = Modifier.fillMaxSize(),

    ) {
        Column(

        )
        {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview(){
    BookingMovieTheme {
        MovieList(navController = rememberNavController())
    }
}