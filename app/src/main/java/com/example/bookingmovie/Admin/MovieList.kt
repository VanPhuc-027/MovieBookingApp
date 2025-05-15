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
import com.example.bookingmovie.ViewModels.GenreViewModel
import com.example.bookingmovie.ViewModels.MovieViewModel
import com.example.bookingmovie.data.Genre.GenreEntity
import com.example.bookingmovie.data.Movie.MovieEntity
import com.example.bookingmovie.data.Movie.MovieWithGenre
import com.example.bookingmovie.ui.theme.BookingMovieTheme
import com.google.accompanist.flowlayout.FlowRow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListContent(
    movies: List<MovieWithGenre>,
    searchText: String,
    selectedGenre: String,
    allGenres: List<String>,
    onSearchTextChange: (String) -> Unit,
    onGenreSelected: (String) -> Unit,
    onEdit: (MovieWithGenre) -> Unit = {},
    onDelete: (MovieWithGenre) -> Unit = {},
    onAdd: () -> Unit = {}
) {
    val filteredMovies = movies.filter {
        (selectedGenre == "Tất cả" || it.genre.firstOrNull()?.genre_name == selectedGenre) &&
                it.movie.movie_name.contains(searchText, ignoreCase = true)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            Text(text = "PHIM", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = onSearchTextChange,
                label = { Text("Tìm kiếm") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Tìm kiếm")
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                allGenres.forEach { genre ->
                    FilterChip(
                        selected = selectedGenre == genre,
                        onClick = { onGenreSelected(genre) },
                        label = { Text(genre, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        shape = RoundedCornerShape(50.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
                        Box(
                            modifier = Modifier
                                .size(width = 100.dp, height = 140.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Gray)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = movie.movie_name, fontWeight = FontWeight.Bold)
                            Text(text = "Thể loại: ${genre.joinToString { it.genre_name }}", fontSize = 12.sp)
                            Text(text = "Mô tả: ${movie.description}", fontSize = 12.sp, maxLines = 2)
                            Text(text = "${movie.price} VND", fontSize = 12.sp, color = Color.Red)
                            Text(text = "Khởi chiếu: ${movie.year}", fontSize = 12.sp, color = Color.Blue)
                        }

                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Sửa", tint = Color.Blue,
                                modifier = Modifier.clickable { onEdit(movieWithGenre) }.size(24.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            Icon(Icons.Default.Delete, contentDescription = "Xoá", tint = Color.Red,
                                modifier = Modifier.clickable { onDelete(movieWithGenre) }.size(24.dp))
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onAdd,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = Color.Blue,
            contentColor = Color.White
        ) {
            Icon(Icons.Default.Add, contentDescription = "Thêm phim")
        }
    }
}

@Composable
fun MovieList(
    viewModel: MovieViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    genreViewModel: GenreViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var searchText by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("Tất cả") }
    //val allGenres = listOf("Tất cả", "Lãng mạn", "Hoạt hình", "Chiến tranh", "Tình cảm", "Hành động")
    val allGenresFromDb by genreViewModel.genreNames.collectAsState()
    val allGenres = listOf("Tất cả") + allGenresFromDb
    val movies by viewModel.allMoviesWithGenre.collectAsState()

    var showAddForm by remember { mutableStateOf(false) }

    if (showAddForm) {
        AddMovieScreen(
            genres = genreViewModel.genres.collectAsState().value,
            onSave = { movie, selectedGenreIds ->
                viewModel.addMovieWithGenres(movie, selectedGenreIds)
                showAddForm = false
            },
            onCancel = { showAddForm = false }
        )
    } else {
        MovieListContent(
            movies = movies,
            searchText = searchText,
            selectedGenre = selectedGenre,
            allGenres = allGenres,
            onSearchTextChange = { searchText = it },
            onGenreSelected = { selectedGenre = it },
            onAdd = { showAddForm = true },
            onEdit = { /* TODO */ },
            onDelete = { /* TODO */ }
        )
    }

}

@Composable
fun AddMovieScreen(
    genres: List<GenreEntity>,
    onSave: (MovieEntity, List<Int>) -> Unit,
    onCancel: () -> Unit
) {
    var movieName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var banner by remember { mutableStateOf("") }
    var video by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    val selectedGenres = remember { mutableStateListOf<Int>() }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Thêm Phim", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = movieName, onValueChange = { movieName = it }, label = { Text("Tên phim") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Mô tả") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Giá vé") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = banner, onValueChange = { banner = it }, label = { Text("Banner URL") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = video, onValueChange = { video = it }, label = { Text("Trailer URL") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = year, onValueChange = { year = it }, label = { Text("Năm sản xuất") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(12.dp))
        Text("Chọn thể loại:", fontWeight = FontWeight.Bold)

        FlowRow(mainAxisSpacing = 8.dp, crossAxisSpacing = 8.dp) {
            genres.forEach { genre ->
                FilterChip(
                    selected = selectedGenres.contains(genre.genre_id),
                    onClick = {
                        if (selectedGenres.contains(genre.genre_id)) {
                            selectedGenres.remove(genre.genre_id)
                        } else {
                            selectedGenres.add(genre.genre_id)
                        }
                    },
                    label = { Text(genre.genre_name, fontSize = 12.sp) },
                    shape = RoundedCornerShape(50)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onCancel, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
                Text("Hủy")
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                if (movieName.isNotBlank() && price.toDoubleOrNull() != null && year.toIntOrNull() != null) {
                    onSave(
                        MovieEntity(
                            movie_name = movieName,
                            description = description,
                            price = price.toDouble(),
                            banner = banner,
                            video = video,
                            year = year.toInt()
                        ),
                        selectedGenres.toList()
                    )
                }
            }) {
                Text("Lưu")
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun MovieListPreview() {
    val mockMovies = listOf(
        MovieWithGenre(
            movie = MovieEntity(1, "Cuộc chiến vô cực",  "2023",100.000,"","",2003),
            genre = listOf(GenreEntity(1, "Hành động","hay"))
        ),
        MovieWithGenre(
            movie = MovieEntity(2, "Tình yêu mùa hạ",  "dien  anh tuyet doi",100.000,"","",2004),
            genre = listOf(GenreEntity(2, "Tình cảm","cũng cũng"))
        )
    )

    BookingMovieTheme {
        MovieListContent(
            movies = mockMovies,
            searchText = "",
            selectedGenre = "Tất cả",
            allGenres = listOf("Tất cả", "Hành động", "Tình cảm"),
            onSearchTextChange = {},
            onGenreSelected = {}
        )
    }
}

