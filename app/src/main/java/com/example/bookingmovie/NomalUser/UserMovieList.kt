package com.example.bookingmovie.NomalUser

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.bookingmovie.ViewModels.MovieViewModel
import com.example.bookingmovie.data.Movie.MovieWithGenre
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import com.example.bookingmovie.MovieUI.Movie.toUIModel

@Composable
fun UserMovieList(
    appNavController: NavHostController,
    movieViewModel: MovieViewModel = viewModel(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Giao diện thanh tìm kiếm "giả"
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { appNavController.navigate("search") },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Tìm kiếm phim...",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        UserMovieListContent(appNavController,movieViewModel)
    }
}


@Composable
fun SearchScreen(
    appNavController: NavHostController,
    movieViewModel: MovieViewModel = viewModel()
) {
    val movieList by movieViewModel.allMoviesWithGenre.collectAsState()

    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var selectedGenre by remember { mutableStateOf("Tất cả") }

    val genres = listOf("Tất cả") + movieList
        .flatMap { it.genre.map { genre -> genre.genre_name } }
        .distinct()

    val filteredMovies = movieList.filter { movieWithGenre ->
        val matchName = movieWithGenre.movie.movie_name.contains(searchText.text, ignoreCase = true)
        val matchGenre = selectedGenre == "Tất cả" || movieWithGenre.genre.any { it.genre_name == selectedGenre }
        matchName && matchGenre
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text("Tìm kiếm phim...") },
            leadingIcon = {
                IconButton(onClick = { appNavController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            genres.forEach { genre ->
                FilterChip(
                    selected = genre == selectedGenre,
                    onClick = { selectedGenre = genre },
                    label = { Text(genre) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Danh sách phim
        if (filteredMovies.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 140.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Không có phim nào.")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredMovies) { movieWithGenre ->
                    MovieGridItem(movieWithGenre,appNavController)
                }
            }
        }
    }
}

@Composable
fun UserMovieListContent(
    appNavController: NavHostController,
    movieViewModel: MovieViewModel = viewModel()
) {
    val movieList by movieViewModel.allMoviesWithGenre.collectAsState()

    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var selectedGenre by remember { mutableStateOf("Tất cả") }

    val genres = listOf("Tất cả") + movieList.flatMap { it.genre.map { g -> g.genre_name } }.distinct()

    val filteredMovies = movieList.filter { movieWithGenre ->
        val matchName = movieWithGenre.movie.movie_name.contains(searchText.text, ignoreCase = true)
        matchName
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        if (filteredMovies.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Không có phim nào.")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredMovies) { movieWithGenre ->
                    MovieGridItem(movieWithGenre,appNavController)
                }
            }
        }
    }
}

@Composable
fun MovieGridItem(
    movieWithGenre: MovieWithGenre,
    appNavController: NavHostController
) {
    val movie = movieWithGenre.movie
    val uiModel = movieWithGenre.toUIModel()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
        .clickable {
            appNavController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("movie", uiModel)
        appNavController.navigate("movie_detail")
    },
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
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = movie.movie_name,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2
        )
    }
}


