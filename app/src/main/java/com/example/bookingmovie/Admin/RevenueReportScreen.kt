package com.example.bookingmovie.Admin

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookingmovie.ViewModels.RevenueViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevenueReportScreen(
    viewModel: RevenueViewModel,
    onBack: () -> Unit
) {
    val revenueData by viewModel.revenueList

    LaunchedEffect(Unit) {
        viewModel.loadRevenue()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(
                    "Báo cáo doanh thu"
                    , color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold
                ) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
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
        ) {
            Text("Doanh thu theo tháng", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            Spacer(modifier = Modifier.height(8.dp))

            revenueData.forEach {
                val month = "Tháng ${it.month.toInt()}" // Convert "01" → "Tháng 1"
                val formattedRevenue = "%,.0f VNĐ".format(it.revenue)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(month)
                    Text(formattedRevenue, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
