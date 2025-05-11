package com.example.bookingmovie

import android.accounts.Account
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieAccount() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("QUẢN LÝ TÀI KHOẢN", color = Color.White)
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
            // Phần hiển thị email + avatar
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(40.dp),
                    tint = Color.Gray
                )
                Column {
                    Text("hoaiquy33@admin.com", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            // Item: Báo cáo doanh thu
            AccountOptionItem(
                icon = Icons.Default.BarChart,
                text = "Báo cáo doanh thu",
                onClick = { /* TODO */ }
            )

            // Item: Đổi mật khẩu
            AccountOptionItem(
                icon = Icons.Default.Lock,
                text = "Đổi mật khẩu",
                onClick = { /* TODO */ }
            )

            // Item: Đăng xuất
            AccountOptionItem(
                icon = Icons.Default.Logout,
                text = "Đăng xuất",
                onClick = { /* TODO */ }
            )
        }
    }
}

@Composable
fun AccountOptionItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.padding(end = 12.dp),
            tint = Color.Black
        )
        Text(text, fontSize = 16.sp)
    }
}

@Preview
@Composable
fun Preview2(){
    MovieAccount()
}