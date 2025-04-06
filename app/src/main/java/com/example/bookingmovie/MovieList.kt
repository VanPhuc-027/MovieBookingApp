package com.example.bookingmovie

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.bookingmovie.ui.theme.BookingMovieTheme

val movies = listOf(
    "Inception",
    "Interstellar",
    "The Dark Knight",
    "Tenet",
    "Dunkirk",
    "Oppenheimer",
    "Batman Begins",
    "The Prestige",
    "Inception",
    "Interstellar",
    "The Dark Knight",
    "Tenet",
    "Dunkirk",
    "Oppenheimer",
    "Batman Begins",
    "The Prestige",
    "Inception",
    "Interstellar",
    "The Dark Knight",
    "Tenet",
    "Dunkirk",
    "Oppenheimer",
    "Batman Begins",
    "The Prestige",
    "Inception",
    "Interstellar",
    "The Dark Knight",
    "Tenet",
    "Dunkirk",
    "Oppenheimer",
    "Batman Begins",
    "The Prestige"
)
@Composable
fun MovieList(){

        LazyColumn(modifier = Modifier.fillMaxSize())
        {
            items(movies.size) { index ->
                Text(
                    text = movies[index],
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
}

@Preview(showBackground = true)
@Composable
fun Preview1(){
    BookingMovieTheme {
        MovieList()
    }
}