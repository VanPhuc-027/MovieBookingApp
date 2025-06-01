    package com.example.bookingmovie.Admin

    import androidx.compose.foundation.Image
    import androidx.compose.foundation.background
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.PaddingValues
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.layout.width
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
    import androidx.compose.runtime.remember
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.foundation.lazy.items
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.material.MaterialTheme
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateListOf
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.graphics.asImageBitmap
    import androidx.compose.ui.window.Dialog
    import com.example.bookingmovie.ViewModels.BookingViewModel
    import com.example.bookingmovie.data.Booking.BookingWithMovieAndUser
    import kotlinx.coroutines.flow.collectLatest
    import com.example.bookingmovie.generateQrCode

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BookingHistory(bookingViewModel: BookingViewModel) {
        val bookings = remember { mutableStateListOf<BookingWithMovieAndUser>() }

        LaunchedEffect(Unit) {
            bookingViewModel.getAllBookingsWithMovieAndUser().collectLatest { list ->
                bookings.clear()
                bookings.addAll(list)
            }
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
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                items(bookings) { booking ->
                    AdminBookingItem(booking)
                }
            }
        }
    }
    @Composable
    fun AdminBookingItem(booking: BookingWithMovieAndUser) {
        var isQrCodeZoomed by remember { mutableStateOf(false) }
        val backgroundColor = if (booking.status == "Hết hạn") Color.LightGray else Color.White
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = MaterialTheme.shapes.medium
        )
        {
            Row (modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Tài khoản: ${booking.userName}", fontWeight = FontWeight.Bold)
                    Text("Movie: ${booking.movieName}")
                    Text("Ngày khởi chiếu: ${booking.showDate} ")
                    Text("Giờ chiếu: ${booking.showTime}")
                    Text("Phòng: ${booking.roomNumber}")
                    Text("Ghế đã chọn: ${booking.selectedSeats}")
                    Text("Đồ ăn kèm: ${booking.selectedFood}")
                    Text("Phương thức thanh toán: ${booking.paymentMethod}")
                    Text("Thời gian đặt: ${booking.bookingTime}")
                    Text("Tổng tiền: ${"%,.0f".format(booking.totalPrice)} VND")
                }
                    Spacer(modifier = Modifier.width(8.dp))
                    QRCode(
                        qrCodeContent = booking.qrCodeContent,
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
                                qrCodeContent = booking.qrCodeContent,
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
            generateQrCode(qrCodeContent,300)
        }
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Mã QR đặt vé",
                modifier = modifier
            )
        }
    }

    @Preview
    @Composable
    fun BookingHistoryPreview(){
        //BookingHistory()
    }
