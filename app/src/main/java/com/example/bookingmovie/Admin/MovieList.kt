package com.example.bookingmovie.Admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookingmovie.R
import com.example.bookingmovie.ViewModels.MovieViewModel
import com.example.bookingmovie.ui.theme.BookingMovieTheme
import com.google.accompanist.flowlayout.FlowRow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieList(viewModel: MovieViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var searchText by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("Tất cả") }

    val allGenres = listOf("Tất cả", "Lãng mạn", "Hoạt hình", "Chiến tranh", "Tình cảm", "Hành động")

    val movies by viewModel.allMoviesWithGenre.collectAsState()

    val filteredMovies = movies.filter {
        (selectedGenre == "Tất cả" || it.genre.firstOrNull()?.genre_name == selectedGenre) &&
                it.movie.movie_name.contains(searchText, ignoreCase = true)
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            // Tiêu đề
            Text(
                text = "PHIM",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tìm kiếm
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Tìm kiếm") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Tìm kiếm"
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Thể loại
            FlowRow(
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                allGenres.forEach { genre ->
                    FilterChip(
                        selected = selectedGenre == genre,
                        onClick = { selectedGenre = genre },
                        label = {
                            Text(
                                text = genre,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        shape = RoundedCornerShape(50.dp),
                        colors = FilterChipDefaults.filterChipColors()
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Danh sách phim
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredMovies, key = { it.movie.movie_id }) { movieWithGenre ->
                    val movie = movieWithGenre.movie
                    val genre = movieWithGenre.genre

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .padding(8.dp)
                    ) {
                        // TODO: Load ảnh banner từ URL nếu có
                        Box(
                            modifier = Modifier
                                .size(width = 100.dp, height = 140.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Gray) // Placeholder
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = movie.movie_name, fontWeight = FontWeight.Bold)
                            Text(text = "Thể loại: ${movieWithGenre.genre}", fontSize = 12.sp)
                            Text(text = movie.description, fontSize = 12.sp, maxLines = 2)
                            Text(text = "${movie.price} VND", fontSize = 12.sp, color = Color.Red)
                            Text(text = "Khởi chiếu: ${movie.year}", fontSize = 12.sp, color = Color.Blue)
                        }

                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Sửa",
                                tint = Color.Blue,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        // TODO: xử lý sửa
                                    }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Xoá",
                                tint = Color.Red,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        // TODO: xử lý xóa
                                    }
                            )
                        }
                    }
                }
            }
        }

        // Nút thêm phim
        FloatingActionButton(
            onClick = {
                // TODO: chuyển sang màn hình thêm phim
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color.Blue,
            contentColor = Color.White
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Thêm phim")
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
