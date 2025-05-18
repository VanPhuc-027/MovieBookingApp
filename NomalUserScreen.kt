package com.example.bookingmovie.NomalUser

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.bookingmovie.R
import com.example.bookingmovie.NormalUser.MovieDetailScreen

@Composable
fun NormalUserScreen(onMovieClick: () -> Unit, navController: NavController) {
    Scaffold(bottomBar = { BottomNavigationBar(navController) }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            SearchBar()
            Spacer(modifier = Modifier.height(12.dp))
            MovieSlider()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Thể loại", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            GenreRow()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Tất cả phim", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            AllMoviesGrid(onMovieClick)
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    val sampleMovie = Movie(
        title = "Strange World (2022)",
        genre = "Tình Cảm",
        releaseDate = "24-11-2022",
        price = "120.000 VNĐ / 1 vé",
        description = "A journey deep into an uncharted and treacherous land...",
        posterResId = R.drawable.poster_mat_biec,
        views = 20
    )

    NavHost(navController, startDestination = "home") {
        composable("home") {
            NormalUserScreen(
                onMovieClick = { navController.navigate("detail") },
                navController = navController
            )
        }
        composable("detail") {
            MovieDetailScreen(
                movie = sampleMovie,
                onBack = { navController.popBackStack() },
                onBook = { /* TODO: Đặt vé */ }
            )
        }
        composable("account") {
            UserAccountScreen(
                onHistoryClick = { navController.navigate("history") }
            )
        }
        composable("history") {
            UserHistoryScreen(onBack = { navController.popBackStack() })
        }
    }
}

@Composable
fun AllMoviesGrid(onMovieClick: () -> Unit) {
    val movies = listOf(
        "Hai Chu Cho Sói" to 17,
        "Titalic" to 6,
        "Doi Khong Nhu Mo" to 9,
        "Phim 4" to 0
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxHeight()
    ) {
        items(movies) { movie ->
            Card(
                modifier = Modifier
                    .height(180.dp)
                    .clickable { onMovieClick() },
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        "${movie.first} - ${movie.second} 👁",
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = { Text("Tìm phim...") },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        singleLine = true
    )
}

@Composable
fun MovieSlider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        Text("Slider phim", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun GenreRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf("Hành động", "Tình cảm", "Hài", "Kinh dị").forEach { genre ->
            AssistChip(
                onClick = { /* TODO */ },
                label = { Text(genre) }
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("home") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Trang chủ") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("account") },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Tài khoản") },
            label = { Text("Account") }
        )
    }
}
