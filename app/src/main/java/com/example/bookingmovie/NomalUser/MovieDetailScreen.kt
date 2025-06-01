package com.example.bookingmovie.NomalUser

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.rememberAsyncImagePainter
import com.example.bookingmovie.MovieUI.Movie.GenreUIModel
import com.example.bookingmovie.MovieUI.Movie.MovieUIModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movie: MovieUIModel,
    onBack: () -> Unit,
    onBook: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Chi tiết phim") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0D1B2A),
                    titleContentColor = Color.White
                ),
                modifier = Modifier.height(52.dp)
            )
        },
        bottomBar = {
            Surface(shadowElevation = 4.dp) {
                Button(
                    onClick = onBook,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("ĐẶT VÉ", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = movie.banner),
                    contentDescription = "Banner phim",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(movie.movie_name, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Thể loại: ${
                    if (movie.genres.isNotEmpty()) movie.genres.joinToString { it.genre_name }
                    else "Chưa cập nhật"
                }",
                style = MaterialTheme.typography.bodyMedium
            )
            Text("Năm sản xuất: ${movie.year}", style = MaterialTheme.typography.bodyMedium)
            Text("Giá vé: ${movie.price} VNĐ", color = Color.Red, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(16.dp))

            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            Text("Thông tin chiếu phim", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Ngày chiếu: ${movie.releaseDate}")
            Text("Phòng chiếu: Tất cả phòng")
            Text("Giờ chiếu: Mọi khung giờ")

            Spacer(modifier = Modifier.height(16.dp))

            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            Text("Mô tả phim", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(movie.description)

            Spacer(modifier = Modifier.height(16.dp))

            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            Text("Video Trailer", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            val videoId = extractYoutubeVideoId(movie.video.toString())
            YouTubePlayerScreen(
                videoId = videoId,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}

fun extractYoutubeVideoId(url: String): String {
    val regex = Regex("(?:v=|youtu\\.be/)([\\w-]{11})")
    return regex.find(url)?.groupValues?.get(1) ?: ""
}

@Composable
fun YouTubePlayerScreen(videoId: String, modifier: Modifier = Modifier) {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        modifier = modifier,
        factory = { context ->
            YouTubePlayerView(context).apply {
                lifecycleOwner.lifecycle.addObserver(this)

                addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.cueVideo(videoId, 0f)
                    }
                })
            }
        }
    )
}




@Preview()
@Composable
fun Prreview5(){
    val mockMovie = MovieUIModel(
        movie_id = 1,
        movie_name = "Lật Mặt 7",
        description = "Một bộ phim hành động kịch tính về tình bạn và phản bội...",
        price = 90000.0,
        banner = "https://media.themoviedb.org/t/p/w600_and_h900_bestv2/wRrGBv4uNofBVyShxfS0iugbcm8.jpg",
        video = "https://www.youtube.com/watch?v=dQw4w9WgXcQ", // Giả sử có trailer
        year = 2025,
        genres = listOf(
            GenreUIModel(1, "Hành động"),
            GenreUIModel(2, "Tâm lý")
        ),
        releaseDate = "2025-12-25"
    )

    MovieDetailScreen(
        movie = mockMovie,
        onBack = {},
        onBook = {}
    )
}
