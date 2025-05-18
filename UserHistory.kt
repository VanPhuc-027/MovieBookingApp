package com.example.bookingmovie.NomalUser

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Booking(
    val movieTitle: String,
    val cinemaName: String,
    val date: String,
    val time: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingHistoryScreen() {
    val bookings = remember {
        listOf(
            Booking("Dự Án Mật: Thảm Họa Trên Cầu", "Galaxy Trung Chánh", "22/07", "20:00"),
            Booking("Aquaman: Vương Quốc Thất Lạc", "Beta Quang Trung", "22/12", "16:10"),
            Booking("Đất Rừng Phương Nam", "Beta Quang Trung", "19/10", "20:30"),
            Booking("Vong Nhi", "Lotte Cộng Hòa", "05/02", "20:10")
        )
    }

    Scaffold(
        containerColor = Color(0xFFF5F5F5), // Nền xám nhạt
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Lịch sử đặt chỗ",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { /* TODO: Xử lý lọc vé */ },
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = "Bộ lọc")
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 80.dp // chừa chỗ cho nút Bộ lọc
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            items(bookings) { booking ->
                BookingItem(booking)
            }
        }
    }
}

@Composable
fun BookingItem(booking: Booking) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = booking.movieTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = booking.cinemaName,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${booking.time} - ${booking.date}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingHistoryScreenPreview() {
    BookingHistoryScreen()
}