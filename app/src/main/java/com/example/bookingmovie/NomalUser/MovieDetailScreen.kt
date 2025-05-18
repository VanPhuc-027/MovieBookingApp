package com.example.bookingmovie.NomalUser

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.ui.tooling.preview.Preview

data class Movie(
    val title: String,
    val genre: String,
    val releaseDate: String,
    val price: String,
    val description: String,
    val posterResId: Int,
    val views: Int
)

@Composable
fun MovieDetailScreen(movie: Movie, onBack: () -> Unit, onBook: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chi tiết phim") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = onBook,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("ĐẶT VÉ")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = movie.posterResId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(movie.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text("Thể loại: ${movie.genre}")
            Text("Khởi chiếu: ${movie.releaseDate}")
            Text("Giá vé: ${movie.price}", color = Color.Red)

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { /* TODO: mở trailer */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
            ) {
                Text("XEM TRAILER", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Thông tin chiếu phim", fontWeight = FontWeight.Bold)
            Text("Phòng chiếu: Tất cả phòng")
            Text("Giờ chiếu: Mọi khung giờ")

            Spacer(modifier = Modifier.height(16.dp))

            Text("Mô tả phim", fontWeight = FontWeight.Bold)
            Text(movie.description)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Video Trailer", fontWeight = FontWeight.Bold)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.PlayCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}
@Preview()
@Composable
fun Prreview5(){

}
