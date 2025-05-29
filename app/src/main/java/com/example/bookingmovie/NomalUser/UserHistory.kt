package com.example.bookingmovie.NomalUser

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.bookingmovie.ViewModels.BookingViewModel
import com.example.bookingmovie.data.Booking.BookingWithMovie
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingHistoryScreen(bookingViewModel: BookingViewModel, currentUserId: Int) {
    val bookings = remember { mutableStateListOf<BookingWithMovie>() }

    LaunchedEffect(currentUserId) {
        bookingViewModel.getUserBookingWithMovie(currentUserId).collectLatest { list ->
            bookings.clear()
            bookings.addAll(list)
        }
    }

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Lịch sử đặt chỗ",
                        color = Color.White,
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0D1B2A)
                ),
                modifier = Modifier.height(52.dp)
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
                bottom = 80.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            items(bookings) { booking ->
                BookingItem(
                    movieTitle = booking.movieName,
                    showDate = booking.showDate,
                    showTime = booking.showTime,
                    bookingTime = booking.bookingTime,
                    totalPrice = booking.totalPrice,
                    qrCodeContent = booking.qrCodeContent,
                    selectedSeats = booking.selectedSeats,
                    selectedFood = booking.selectedFood,
                    paymentMethod = booking.paymentMethod,
                    roomNumber = booking.roomNumber
                )
            }
        }
    }
}

@Composable
fun BookingItem(
    movieTitle: String,
    showDate: String,
    showTime: String,
    bookingTime: String,
    totalPrice: Double,
    qrCodeContent: String,
    selectedSeats: String,
    selectedFood: String,
    paymentMethod: String,
    roomNumber: String
) {
    var isQrCodeZoomed by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row (modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = movieTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Ngày chiếu: $showDate - Giờ: $showTime", fontSize = 14.sp, color = Color.Gray)
                Text(text = "Phòng: $roomNumber", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Ghế đã chọn: $selectedSeats", fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                if (selectedFood.isNotBlank()) {
                    Text(text = "Đồ ăn kèm: $selectedFood", fontSize = 13.sp)
                } else {
                    Text(text = "Không chọn đồ ăn kèm", fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Phương thức thanh toán: $paymentMethod", fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Thời gian đặt: $bookingTime", fontSize = 13.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Tổng tiền: ${"%,.0f".format(totalPrice)} VND", fontSize = 13.sp, color = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            QRCode(
                qrCodeContent = qrCodeContent,
                modifier = Modifier
                    .size(100.dp)
                    .clickable { isQrCodeZoomed = true }
            )
        }
        if (isQrCodeZoomed) {
            Dialog(
                onDismissRequest = { isQrCodeZoomed = false }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    QRCode(
                        qrCodeContent = qrCodeContent,
                        modifier = Modifier
                            .size(300.dp)
                            .clickable { isQrCodeZoomed = false }
                    )
                }
            }
        }
    }
}

@Composable
fun QRCode(qrCodeContent: String, modifier: Modifier = Modifier) {
    val bitmap = remember(qrCodeContent) {
        generateQRCodeBitmap(qrCodeContent,300)
    }
    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Mã QR đặt vé",
            modifier = modifier
        )
    }
}

fun generateQRCodeBitmap(text: String, size: Int): Bitmap? {
    return try {
        val bitMatrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size)
        val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        bmp
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Preview(showBackground = true)
@Composable
fun BookingHistoryScreenPreview() {
    //BookingHistoryScreen()
}