package com.example.bookingmovie.NomalUser

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieCategoryScreen() {

    val categories = listOf(
        "Anime", "Bí Ẩn", "Chiến Tranh", "Chiếu Rạp", "Chuyển Thể", "Chính Kịch", "Chính Luận", "Chính Trị",
        "Chương Trình Truyền Hình", "Cung Đấu", "Cuối Tuần", "Cách Mạng", "Cổ Trang", "Cổ Tích", "Cổ Điển", "DC",
        "Disney", "Gay Cấn", "Gia Đình", "Giáng Sinh", "Giả Tưởng", "Hoàng Cung", "Hoạt Hình", "Hài",
        "Hành Động", "Hình Sự", "Học Đường", "Khoa Học", "Kinh Dị", "Kinh Điển", "Kịch Nói", "Kỳ Ảo",
        "LGBT+", "Lãng Mạn", "Lịch Sử", "Marvel", "Miền Viễn Tây", "Nghề Nghiệp", "Nhạc Kịch", "Phiêu Lưu",
        "Phép Thuật", "Siêu Anh Hùng", "Thiếu Nhi", "Thần Thoại", "Thể Thao", "Truyền Hình Thực Tế", "Tuổi Trẻ",
        "Tài Liệu", "Tâm Lý", "Tình Cảm", "Tập Luyện", "Viễn Tưởng", "Võ Thuật", "Xuyên Không", "Đau Thương", "Đời Thường"
    )

    Scaffold(
        containerColor = Color(0xFF0D0D0D),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Thể loại phim",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1B263B)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4), // 4 cột
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    items(categories) { category ->
                        CategoryItem(
                            name = category,
                            onClick = {
                                // TODO: Xử lý khi click vào thể loại
                            }
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun CategoryItem(name: String, onClick: () -> Unit) {
    Text(
        text = name,
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp),
        color = Color.White,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewMovieCategoryScreen() {
    MovieCategoryScreen()
}