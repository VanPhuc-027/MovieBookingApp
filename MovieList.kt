package com.example.bookingmovie

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookingmovie.ui.theme.BookingMovieTheme

// Danh sách phim
val movies = listOf(
    "Thám Tử Kiện: Kỳ Án Không Đầu",
    "Lật Mặt 8: Vòng Tay Nắng",
    "Cười Ma 2: Giải Hạn",
    "Đất Rừng Phương Nam"
)

// Ánh xạ tên phim -> id poster
val movieImages = mapOf(
    "Thám Tử Kiện: Kỳ Án Không Đầu" to R.drawable.poster_tham_tu_kien,
    "Lật Mặt 8: Vòng Tay Nắng" to R.drawable.poster_lat_mat_8,
    "Cười Ma 2: Giải Hạn" to R.drawable.poster_cuoi_ma_2,
    "Đất Rừng Phương Nam" to R.drawable.poster_dat_rung_phuong_nam
)

@Composable
fun MovieList() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(8.dp)
    ) {
        Text(
            text = "Đang chiếu",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0D47A1),
            modifier = Modifier.padding(8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(movies) { movie ->
                MovieItem(
                    movieName = movie,
                    movieImages = movieImages // <--- truyền map vào
                )
            }
        }
    }
}

@Composable
fun MovieItem(
    movieName: String,
    movieImages: Map<String, Int> // <--- nhận map từ ngoài vào
) {
    val imageResId = movieImages[movieName] ?: R.drawable.poster_lat_mat_8 // fallback nếu lỗi

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable { /* TODO: Xử lý click vào phim */ }
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = movieName,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = movieName,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            maxLines = 2
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMovieList() {
    BookingMovieTheme {
        MovieList()
    }
}
