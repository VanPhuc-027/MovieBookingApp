    package com.example.bookingmovie.Admin

    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.PaddingValues
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
    import androidx.compose.material.MaterialTheme
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateListOf
    import androidx.compose.runtime.setValue
    import com.example.bookingmovie.ViewModels.BookingViewModel
    import com.example.bookingmovie.data.Booking.BookingWithMovieAndUser
    import kotlinx.coroutines.flow.collectLatest

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
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("User: ${booking.userName}", fontWeight = FontWeight.Bold)
                Text("Movie: ${booking.movieName}")
                Text("Date: ${booking.showDate} - Time: ${booking.showTime}")
                Text("Room: ${booking.roomNumber}")
                Text("Seats: ${booking.selectedSeats}")
                Text("Food: ${booking.selectedFood}")
                Text("Payment: ${booking.paymentMethod}")
                Text("Booking Time: ${booking.bookingTime}")
                Text("Total: ${"%,.0f".format(booking.totalPrice)} VND")
                // Optionally show QR code as in UserHistory
            }
        }
    }

    @Preview
    @Composable
    fun BookingHistoryPreview(){
        //BookingHistory()
    }
