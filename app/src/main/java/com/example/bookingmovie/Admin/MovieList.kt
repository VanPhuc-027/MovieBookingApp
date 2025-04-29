package com.example.bookingmovie.Admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookingmovie.ui.theme.BookingMovieTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.ui.draw.alpha

val movies = listOf(
    "Inception", "Interstellar", "The Dark Knight", "Tenet",
    "Dunkirk", "Oppenheimer", "Avengers: Endgame", "Kungfu Panda 4",
    "Inception", "Joker", "The Dark Knight", "Tenet"
)

@Composable
fun MovieList() {
    var showAll by remember { mutableStateOf(false) }
    var isSearchExpanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val filteredMovies = movies.filter { it.contains(searchText, ignoreCase = true) }
    val displayedMovies = if (showAll) filteredMovies else filteredMovies.take(4)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B263B))
            .padding(16.dp)
    ) {
        Text(
            text = "Quản lý phim",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            if (isSearchExpanded) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Tìm kiếm phim...", fontSize = 14.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 14.sp)
                )
            }

            IconButton(
                onClick = {
                    isSearchExpanded = !isSearchExpanded
                    if (!isSearchExpanded) searchText = ""
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Tìm kiếm",
                    tint = Color.White
                )
            }
        }

        Button(
            onClick = { /* TODO: Thêm phim */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF1976D2),
                contentColor = Color.White
            )
        ) {
            Text("➕ Thêm phim")
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(displayedMovies.size) { index ->
                Card(
                    backgroundColor = Color.White,
                    elevation = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = displayedMovies[index],
                            color = Color(0xFF0D47A1),
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { /* TODO: Sửa */ },
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text("✏️ Sửa")
                            }
                            Button(
                                onClick = { /* TODO: Xoá */ },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(0xFFD32F2F),
                                    contentColor = Color.White
                                )
                            ) {
                                Text("🗑️ Xoá")
                            }
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = true,
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            Text(
                text = if (showAll) "Thu gọn ▲" else "Xem thêm ▼",
                color = Color(0xFF64B5F6),
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .alpha(0.85f) // hiệu ứng mờ ảo nhẹ
                    .padding(top = 12.dp)
                    .clickable { showAll = !showAll }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieListPreview() {
    BookingMovieTheme {
        MovieList()
    }
}
