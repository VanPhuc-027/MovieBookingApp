package com.example.bookingmovie.NomalUser

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.bookingmovie.ViewModels.MovieViewModel
import com.example.bookingmovie.data.Movie.MovieEntity
import com.example.bookingmovie.data.Movie.MovieWithGenre

@Composable
fun UserMovieList(
    onMovieClick: (MovieEntity) -> Unit,
    movieViewModel: MovieViewModel = viewModel()
) {
    val movieList by movieViewModel.allMoviesWithGenre.collectAsState()

    Scaffold(
        topBar = {

        }
    ) { padding ->
        if (movieList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Không có phim nào.")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(movieList) { movieWithGenre ->
                    MovieGridItem(
                        movieWithGenre = movieWithGenre,
                        onClick = {onMovieClick(movieWithGenre.movie)}
                    )
                }
            }
        }
    }
}

@Composable
fun MovieGridItem(
    onClick: () -> Unit,
    movieWithGenre: MovieWithGenre
) {
    val movie = movieWithGenre.movie

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(movie.banner),
            contentDescription = movie.movie_name,
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = movie.movie_name,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

