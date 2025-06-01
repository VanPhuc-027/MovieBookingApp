package com.example.bookingmovie.NomalUser

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookingmovie.MovieUI.Movie.MovieUIModel
import com.example.bookingmovie.ViewModels.BookingViewModel
import com.example.bookingmovie.data.Item.ItemEntity
import com.example.bookingmovie.data.Room.RoomEntity
import com.example.bookingmovie.data.Seat.SeatEntity
import com.example.bookingmovie.data.Showtime.ShowtimeEntity
import com.example.bookingmovie.data.User.UserEntity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.bookingmovie.generateQrCode
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Surface
import androidx.compose.ui.text.style.TextAlign

@Composable
fun BookingScreen(
    movie: MovieUIModel,
    currentUser: UserEntity,
    onBack: () -> Unit,
    onBookingSuccess: () -> Unit,
    roomList: List<RoomEntity>,
    seatListProvider: (roomId: Int, showtimeId: Int) -> List<SeatEntity>,
    foodItems: List<ItemEntity>,
    showtimes: List<ShowtimeEntity>,
    viewModel: BookingViewModel
) {
    var selectedRoomId by remember { mutableStateOf(roomList.firstOrNull()?.room_id ?: 0) }
    var selectedSeats by remember { mutableStateOf(mutableSetOf<String>()) }
    val selectedFood = remember { mutableStateMapOf<String, Int>() }
    var paymentMethod by remember { mutableStateOf("Tiền mặt") }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var selectedShowtimeId by remember { mutableStateOf(showtimes.firstOrNull()?.showtimeId ?: 0) }

    val seats = seatListProvider(selectedRoomId, selectedShowtimeId)
    val filteredShowtimes = showtimes.filter { it.roomId == selectedRoomId }
    var showPaypalWebView by remember { mutableStateOf(false) }
    val totalPrice = movie.price * selectedSeats.size + selectedFood.entries.sumOf { (name, qty) ->
        val itemPrice = foodItems.find { it.name == name }?.price ?: 0
        qty * itemPrice
    }

    val qrCodeContent = buildString {
        append("Phim: ${movie.movie_name}\n")
        append("Thời gian: ${movie.releaseDate} - ${filteredShowtimes.find { it.showtimeId == selectedShowtimeId }?.showTime ?: "N/A"}\n")
        append("Phòng: ${roomList.find { it.room_id == selectedRoomId }?.Room_Number ?: "N/A"}\n")
        append("Ghế: ${selectedSeats.joinToString()}\n")
        append("Đồ ăn: ${
            if (selectedFood.isEmpty()) "Không có"
            else selectedFood.entries.joinToString { "${it.key} x${it.value}" }
        }\n")
        append("Thanh toán: $paymentMethod\n")
        append("Tổng tiền: ${totalPrice} VND")
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Movie Title Header
            Text(
                text = movie.movie_name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Room Selection
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Chọn phòng chiếu",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    DropdownMenuBox(
                        items = roomList,
                        selectedId = selectedRoomId,
                        onSelect = { newRoomId ->
                            if (newRoomId != selectedRoomId) {
                                selectedRoomId = newRoomId
                                selectedSeats = mutableSetOf()
                            }
                        }
                    )
                }
            }

            // Showtime Selection
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Chọn suất chiếu",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    DropdownMenuBoxShowtime(
                        items = filteredShowtimes,
                        selectedId = selectedShowtimeId,
                        onSelect = { newShowtimeId ->
                            if (newShowtimeId != selectedShowtimeId) {
                                selectedShowtimeId = newShowtimeId
                                selectedSeats = mutableSetOf()
                            }
                        }
                    )
                }
            }

            // Seat Selection
            SeatGrid(
                seats = seats,
                selectedSeats = selectedSeats,
                onSeatToggle = { seatNumber ->
                    selectedSeats = selectedSeats.toMutableSet().apply {
                        if (contains(seatNumber)) remove(seatNumber) else add(seatNumber)
                    }
                }
            )

            // Food Selection
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Đồ ăn kèm",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    FoodPicker(selectedFood = selectedFood, foodItems = foodItems)
                }
            }

            // Payment Method
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Phương thức thanh toán",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    PaymentMethodSelector(
                        selectedMethod = paymentMethod,
                        onMethodSelected = { paymentMethod = it }
                    )
                }
            }

            // Total Price
            Text(
                text = "Tổng tiền: $totalPrice VND",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )

            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp)
                ) {
                    Text("Hủy", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier.weight(1f).padding(start = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Xác nhận", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }

            // Confirmation Dialog
            if (showConfirmDialog) {
                androidx.compose.material3.AlertDialog(
                    onDismissRequest = { showConfirmDialog = false },
                    title = { Text("Xác nhận đặt vé", style = MaterialTheme.typography.titleLarge) },
                    text = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Tên phim: ${movie.movie_name}", style = MaterialTheme.typography.bodyLarge)
                            Text("Ngày khởi chiếu: ${movie.releaseDate}", style = MaterialTheme.typography.bodyLarge)
                            Text("Số vé: ${selectedSeats.size}", style = MaterialTheme.typography.bodyLarge)
                            Text("Ghế đã chọn: ${selectedSeats.joinToString()}", style = MaterialTheme.typography.bodyLarge)
                            val selectedShowtime = showtimes.find { it.showtimeId == selectedShowtimeId }
                            Text("Khung giờ chiếu: ${selectedShowtime?.showTime ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)
                            val selectedRoom = roomList.find { it.room_id == selectedRoomId }
                            Text("Phòng: ${selectedRoom?.Room_Number ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)

                            if (selectedFood.isNotEmpty()) {
                                Text("Đồ ăn kèm:", style = MaterialTheme.typography.bodyLarge)
                                selectedFood.forEach { (name, qty) ->
                                    if (qty > 0)
                                        Text("- $name: $qty", style = MaterialTheme.typography.bodyMedium)
                                }
                            } else {
                                Text("Không chọn đồ ăn kèm", style = MaterialTheme.typography.bodyMedium)
                            }

                            Text("Phương thức thanh toán: $paymentMethod", style = MaterialTheme.typography.bodyLarge)
                            Text("Tổng tiền: ${totalPrice}đ", style = MaterialTheme.typography.bodyLarge)
                            val qrCodeBitmap = generateQrCode(qrCodeContent)
                            Image(
                                bitmap = qrCodeBitmap.asImageBitmap(),
                                contentDescription = "QR Code",
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .size(150.dp)
                                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (paymentMethod == "Ví điện tử") {
                                    showPaypalWebView = true
                                } else {
                                    val selectedShowtime = showtimes.find { it.showtimeId == selectedShowtimeId }
                                    viewModel.confirmBooking(
                                        userId = currentUser.user_id,
                                        showDate = movie.releaseDate,
                                        showTime = selectedShowtime?.showTime ?: "18:00",
                                        selectedSeats = selectedSeats.toList(),
                                        selectedFood = selectedFood.toMap(),
                                        paymentMethod = paymentMethod,
                                        totalPrice = totalPrice.toDouble(),
                                        bookingTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
                                        status = "Còn hạn",
                                        roomId = selectedRoomId,
                                        showtimeId = selectedShowtimeId,
                                        qrCodeContent = qrCodeContent,
                                        onSuccess = onBookingSuccess
                                    )
                                }
                                showConfirmDialog = false
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Đặt vé", fontSize = 16.sp)
                        }
                    },
                    dismissButton = {
                        OutlinedButton(
                            onClick = { showConfirmDialog = false },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Hủy", fontSize = 16.sp)
                        }
                    },
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }
    }

    // PayPal WebView
    if (showPaypalWebView) {
        PaypalWebViewScreen(
            paypalUrl = "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_xclick&business=sb-fplsw42745067@business.example.com&item_name=Ve+Xem+Phim&amount=3.50&currency_code=USD&return=https://myapp.com/payment/success&cancel_return=https://myapp.com/payment/cancel",

        onSuccess = {
                showPaypalWebView = false
                val selectedShowtime = showtimes.find { it.showtimeId == selectedShowtimeId }
                val currentBookingTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                val totalPrice = movie.price * selectedSeats.size + selectedFood.entries.sumOf { (name, qty) ->
                    val itemPrice = foodItems.find { it.name == name }?.price ?: 0
                    qty * itemPrice
                }
                viewModel.confirmBooking(
                    userId = currentUser.user_id,
                    showDate = movie.releaseDate,
                    showTime = selectedShowtime?.showTime ?: "18:00",
                    selectedSeats = selectedSeats.toList(),
                    selectedFood = selectedFood.toMap(),
                    paymentMethod = paymentMethod,
                    totalPrice = totalPrice.toDouble(),
                    bookingTime = currentBookingTime,
                    status = "Còn hạn",
                    roomId = selectedRoomId,
                    showtimeId = selectedShowtimeId,
                    onSuccess = onBookingSuccess,
                    qrCodeContent = qrCodeContent
                )
            },
            onCancel = { showPaypalWebView = false }
        )
    }
}

@Composable
fun DropdownMenuBoxShowtime(
    items: List<ShowtimeEntity>,
    selectedId: Int,
    onSelect: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedText = items.find { it.showtimeId == selectedId }
        ?.let { "${it.showTime}" } ?: "Chọn suất chiếu"
    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
        ) {
            Text(
                selectedText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            items.forEach {
                DropdownMenuItem(
                    text = { Text("${it.showTime}", style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        onSelect(it.showtimeId)
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun PaymentMethodSelector(
    selectedMethod: String,
    onMethodSelected: (String) -> Unit
) {
    val paymentMethods = listOf("Tiền mặt", "Chuyển khoản", "Ví điện tử")
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
        ) {
            Text(
                selectedMethod,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            paymentMethods.forEach { method ->
                DropdownMenuItem(
                    text = { Text(method, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        onMethodSelected(method)
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun FoodPicker(
    selectedFood: MutableMap<String, Int>,
    foodItems: List<ItemEntity>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        foodItems.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${item.name} (${item.price}đ)",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            val current = selectedFood[item.name] ?: 0
                            if (current > 1) {
                                selectedFood[item.name] = current - 1
                            } else if (current == 1) {
                                selectedFood.remove(item.name)
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.size(36.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("-", fontSize = 16.sp)
                    }
                    Text(
                        text = "${selectedFood[item.name] ?: 0}",
                        modifier = Modifier.padding(horizontal = 12.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Button(
                        onClick = {
                            val current = selectedFood[item.name] ?: 0
                            selectedFood[item.name] = current + 1
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.size(36.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("+", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SeatGrid(
    seats: List<SeatEntity>,
    selectedSeats: Set<String>,
    onSeatToggle: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Màn hình chính",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Column(
                modifier = Modifier
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val groupedSeats = seats.groupBy { it.row }
                groupedSeats.forEach { (_, rowSeats) ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        rowSeats.forEach { seat ->
                            val isSelected = selectedSeats.contains(seat.seatNumber)
                            Button(
                                onClick = { onSeatToggle(seat.seatNumber) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = when {
                                        seat.isBooked -> Color(0xFFBBBBBB)
                                        isSelected -> MaterialTheme.colorScheme.primary
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    },
                                    contentColor = when {
                                        seat.isBooked || isSelected -> Color.White
                                        else -> Color.Black
                                    }
                                ),
                                enabled = !seat.isBooked,
                                modifier = Modifier
                                    .padding(2.dp)
                                    .size(44.dp),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    seat.seatNumber,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownMenuBox(
    items: List<RoomEntity>,
    selectedId: Int,
    onSelect: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedText = items.find { it.room_id == selectedId }?.Room_Number ?: "Chọn phòng"
    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
        ) {
            Text(
                selectedText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            items.forEach {
                DropdownMenuItem(
                    text = { Text(it.Room_Number, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        onSelect(it.room_id)
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun PaypalWebViewScreen(
    paypalUrl: String,
    onSuccess: () -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    var showConfirmDialog by remember { mutableStateOf(false) }
    var paymentSuccess by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AndroidView(
            factory = {
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                            val url = request?.url.toString()
                            return when {
                                url.contains("payment/success") -> {
                                    paymentSuccess = true
                                    showConfirmDialog = true
                                    true
                                }
                                url.contains("payment/cancel") -> {
                                    onCancel()
                                    true
                                }
                                else -> false
                            }
                        }
                    }
                    loadUrl(paypalUrl)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        if (showConfirmDialog && paymentSuccess) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                title = { Text("Xác nhận thanh toán", style = MaterialTheme.typography.titleLarge) },
                text = { Text("Bạn đã thanh toán thành công qua PayPal.", style = MaterialTheme.typography.bodyLarge) },
                confirmButton = {
                    Button(
                        onClick = {
                            showConfirmDialog = false
                            if (paymentSuccess) {
                                onSuccess()
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("OK", fontSize = 16.sp)
                    }
                },
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}