package com.example.bookingmovie.Admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.lazy.items


data class Booking(
    val movieTitle: String,
    val date: String,
    val time: String,
    val seats: List<String>,
    val status: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingHistory() {
    val bookings = remember {
        listOf(
            Booking("Avengers: Endgame", "10/04/2025", "18:00", listOf("A1", "A2"), "Đã thanh toán"),
            Booking("Kungfu Panda 4", "11/04/2025", "20:00", listOf("B3", "B4"), "Chưa thanh toán"),
            Booking("Joker", "09/04/2025", "22:00", listOf("C1"), "Đã huỷ")
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Lịch sử đặt vé", color = Color.White)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0D1B2A)
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
            Text("Danh sách vé đã đặt", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(bookings) { booking ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("🎬 Phim: ${booking.movieTitle}", fontWeight = FontWeight.Bold)
                            Text("📅 Ngày: ${booking.date}")
                            Text("🕐 Giờ: ${booking.time}")
                            Text("💺 Ghế: ${booking.seats.joinToString(", ")}")
                            Text(
                                text = "📌 Trạng thái: ${booking.status}",
                                color = when (booking.status) {
                                    "Đã thanh toán" -> Color(0xFF2E7D32) // xanh lá
                                    "Chưa thanh toán" -> Color(0xFFF9A825) // vàng
                                    "Đã huỷ" -> Color.Red
                                    else -> Color.Black
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun BookingHistoryPreview(){
    BookingHistory()
}
