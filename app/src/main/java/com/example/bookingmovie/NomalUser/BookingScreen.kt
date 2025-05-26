package com.example.bookingmovie.NomalUser

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.example.bookingmovie.MovieUI.Movie.MovieUIModel
import com.example.bookingmovie.ViewModels.BookingViewModel
import com.example.bookingmovie.data.Item.ItemEntity
import com.example.bookingmovie.data.Room.RoomEntity
import com.example.bookingmovie.data.Seat.SeatEntity
import com.example.bookingmovie.data.Showtime.ShowtimeEntity
import com.example.bookingmovie.data.User.UserEntity

@Composable
fun BookingScreen(
    movie: MovieUIModel,
    currentUser: UserEntity,
    onBack: () -> Unit,
    onBookingSuccess: () -> Unit,
    roomList: List<RoomEntity>,
    seatListProvider: (roomId: Int) -> List<SeatEntity>,
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
    val seats = seatListProvider(selectedShowtimeId)

    Spacer(Modifier.height(8.dp))

    Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
        Text("Chọn phòng chiếu", fontWeight = FontWeight.Bold)
        DropdownMenuBox(items = roomList, selectedId = selectedRoomId) { newRoomId ->
            if (newRoomId != selectedRoomId) {
                selectedRoomId = newRoomId
                selectedSeats = mutableSetOf()
            }
        }

        Text("Chọn suất chiếu", fontWeight = FontWeight.Bold)
        DropdownMenuBoxShowtime(
            items = showtimes,
            selectedId = selectedShowtimeId
        ) { newShowtimeId ->
            if (newShowtimeId != selectedShowtimeId) {
                selectedShowtimeId = newShowtimeId
                selectedSeats = mutableSetOf()
            }
        }

        Spacer(Modifier.height(8.dp))
        Spacer(Modifier.height(8.dp))
        Text("Chọn ghế", fontWeight = FontWeight.Bold)
        SeatGrid(
            seats = seats,
            selectedSeats = selectedSeats,
            onSeatToggle = { seatNumber ->
                selectedSeats = selectedSeats.toMutableSet().apply {
                    if (contains(seatNumber)) remove(seatNumber) else add(seatNumber)
                }
            }
        )

        Spacer(Modifier.height(8.dp))
        Text("Đồ ăn kèm", fontWeight = FontWeight.Bold)
        FoodPicker(selectedFood, foodItems = foodItems)

        Spacer(Modifier.height(8.dp))
        Text("Phương thức thanh toán", fontWeight = FontWeight.Bold)
        PaymentMethodSelector(paymentMethod) {
            paymentMethod = it
        }

        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onBack) {
                Text("Huỷ")
            }
            Button(onClick = {
                showConfirmDialog = true
            }) {
                Text("Xác nhận")
            }

            if (showConfirmDialog) {
                androidx.compose.material3.AlertDialog(
                    onDismissRequest = { showConfirmDialog = false },
                    title = { Text("Xác nhận đặt vé") },
                    text = {
                        Column {
                            Text("Tên phim: ${movie.movie_name}")
                            Text("Số vé: ${selectedSeats.size}")
                            Text("Ghế đã chọn: ${selectedSeats.joinToString()}")
                            val selectedShowtime = showtimes.find { it.showtimeId == selectedShowtimeId }
                            Text("Khung giờ chiếu: ${selectedShowtime?.showTime ?: "N/A"}")
                            val selectedRoom = roomList.find { it.room_id == selectedRoomId }
                            Text("Phòng: ${selectedRoom?.Room_Number ?: "N/A"}")

                            if (selectedFood.isNotEmpty()) {
                                Text("Đồ ăn kèm:")
                                selectedFood.forEach { (name, qty) ->
                                    if (qty > 0)
                                        Text("- $name: $qty")
                                }
                            } else {
                                Text("Không chọn đồ ăn kèm")
                            }

                            Text("Phương thức thanh toán: $paymentMethod")

                            val totalPrice = movie.price * selectedSeats.size + selectedFood.entries.sumOf { (name, qty) ->
                                val itemPrice = foodItems.find { it.name == name }?.price ?: 0
                                qty * itemPrice
                            }
                            Text("Tổng tiền: ${totalPrice}đ")
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            showConfirmDialog = false

                            val selectedSeatEntities = seats.filter { selectedSeats.contains(it.seatNumber) }
                            val selectedShowtime = showtimes.find { it.showtimeId == selectedShowtimeId }
                            val totalPrice = movie.price * selectedSeats.size + selectedFood.entries.sumOf { (name, qty) ->
                                val itemPrice = foodItems.find { it.name == name }?.price ?: 0
                                qty * itemPrice
                            }
                            viewModel.confirmBooking(
                                userId = currentUser.user_id,
                                showDate = "2025-06-01",
                                showTime = selectedShowtime?.showTime ?: "18:00",
                                selectedSeats = selectedSeats.toList(),
                                selectedFood = selectedFood.toMap(),
                                paymentMethod = paymentMethod,
                                totalPrice = totalPrice.toDouble(),
                                bookingTime = "643",
                                status = "Còn hạn",
                                roomId = selectedRoomId,
                                onSuccess = onBookingSuccess
                            )
                        }) {
                            Text("Đặt vé")
                        }
                    },
                    dismissButton = {
                        OutlinedButton(onClick = { showConfirmDialog = false }) {
                            Text("Hủy")
                        }
                    }
                )
            }
        }
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
        ?.let {"${it.showTime}" } ?: "Chọn suất chiếu"
    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selectedText)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach {
                DropdownMenuItem(
                    text = { Text("${it.showTime}") },
                    onClick = {
                        onSelect(it.showtimeId)
                        expanded = false
                    }
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
        OutlinedButton(onClick = { expanded = true }) {
            Text(selectedMethod)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            paymentMethods.forEach { method ->
                DropdownMenuItem(
                    text = { Text(method) },
                    onClick = {
                        onMethodSelected(method)
                        expanded = false
                    }
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
    Column {
        foodItems.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${item.name} (${item.price}đ)", fontWeight = FontWeight.SemiBold)

                Row {
                    Button(onClick = {
                        val current = selectedFood[item.name] ?: 0
                        if (current > 0) selectedFood[item.name] = current - 1
                    }) {
                        Text("-")
                    }
                    Text(
                        text = "${selectedFood[item.name] ?: 0}",
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Button(onClick = {
                        val current = selectedFood[item.name] ?: 0
                        selectedFood[item.name] = current + 1
                    }) {
                        Text("+")
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
    val groupedSeats = seats.groupBy { it.row }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Màn hình chính",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                groupedSeats.forEach { (_, rowSeats) ->
                    Row {
                        rowSeats.forEach { seat ->
                            val isSelected = selectedSeats.contains(seat.seatNumber)
                            Button(
                                onClick = {
                                    onSeatToggle(seat.seatNumber)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = when {
                                        seat.isBooked -> Color.LightGray
                                        isSelected -> Color.Green
                                        else -> Color.White
                                    },
                                    contentColor = Color.Black
                                ),
                                enabled = !seat.isBooked,
                                modifier = Modifier
                                    .padding(2.dp)
                                    .height(36.dp)
                            ) {
                                Text(seat.seatNumber)
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
    var selectedText = items.find { it.room_id == selectedId }?.Room_Number ?: "Chọn phòng"
    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selectedText)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach {
                DropdownMenuItem(text = { Text(it.Room_Number) }, onClick = {
                    onSelect(it.room_id)
                    expanded = false
                })
            }
        }
    }
}


