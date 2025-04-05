package com.example.bookingmovie

import androidx.compose.foundation.layout.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieCategory() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Trang chủ",
                        color = Color.White,
                    )
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
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Thể loại", fontSize = 18.sp)
            // Danh sách sẽ để ở đây
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMovieCategory() {
    MovieCategory()
}
