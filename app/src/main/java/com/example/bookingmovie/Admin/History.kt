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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material.Divider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingHistory() {
    var searchId by remember { mutableStateOf("") }

    // Dữ liệu mẫu
    val bookings = remember {
        listOf(
            BookingDetail(
                id = "1675088298728",
                email = "tin@gmail.com",
                movieTitle = "Hai Chu Cho Sói",
                date = "18-01-2023",
                room = "Phòng 2",
                time = "10AM - 11AM",
                quantity = 2,
                seats = listOf("4", "5"),
                food = "Không",
                payment = "PayPal",
                total = "100 000 VND"
            ),
            BookingDetail(
                id = "1675088288014",
                email = "tin@gmail.com",
                movieTitle = "Hai Chu Cho Sói",
                date = "18-01-2023",
                room = "Phòng 2",
                time = "10AM - 11AM",
                quantity = 2,
                seats = listOf("6", "7"),
                food = "Không",
                payment = "PayPal",
                total = "100 000 VND"
            )
        )
    }

    val filteredBookings = bookings.filter {
        it.id.contains(searchId, ignoreCase = true)
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
            // Ô tìm kiếm theo mã vé
            OutlinedTextField(
                value = searchId,
                onValueChange = { searchId = it },
                placeholder = { Text("ID...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Tìm kiếm"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredBookings) { booking ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Mã: ${booking.id}")
                            Text("Email: ${booking.email}")
                            Text("Tên phim: ${booking.movieTitle}")
                            Text("Khởi chiếu: ${booking.date}")
                            Text("Phòng chiếu: ${booking.room}")
                            Text("Giờ chiếu: ${booking.time}")
                            Text("Số lượng vé: ${booking.quantity}")
                            Text("Ghế đã chọn: ${booking.seats.joinToString(", ")}")
                            Text("Đồ ăn/uống: ${booking.food}")
                            Text("Thanh toán: ${booking.payment}")

                            Spacer(modifier = Modifier.height(4.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Tổng tiền: ${booking.total}",
                                color = Color.Red,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

data class BookingDetail(
    val id: String,
    val email: String,
    val movieTitle: String,
    val date: String,
    val room: String,
    val time: String,
    val quantity: Int,
    val seats: List<String>,
    val food: String,
    val payment: String,
    val total: String
)

@Preview
@Composable
fun BookingHistoryPreview(){
    BookingHistory()
}
