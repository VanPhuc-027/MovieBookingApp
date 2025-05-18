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
@Composable
fun UserHistoryScreen(onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Lịch sử đặt vé", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text("Quay lại")
        }

        // Danh sách vé giả lập
        val tickets = listOf(
            "Vé 1 - Spider-Man - 10/10/2024",
            "Vé 2 - Titanic - 12/10/2024"
        )

        tickets.forEach {
            Text(it, modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}
