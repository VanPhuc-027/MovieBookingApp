package com.example.bookingmovie

import android.accounts.Account
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
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
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieAccount() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Tài khoản",
                        color = Color.White
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
            Text(text = "👤 Tài khoản: admin01", fontSize = 18.sp)
            Text(text = "🔐 Vai trò: Quản trị viên", fontSize = 14.sp, color = Color.Gray)

            androidx.compose.material3.Divider(modifier = Modifier.padding(vertical = 12.dp))

            // Nút Đổi mật khẩu
            androidx.compose.material3.Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Text("Đổi mật khẩu")
            }

            // Nút Đăng xuất
            androidx.compose.material3.Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Đăng xuất", color = Color.White)
            }
        }
    }
}

@Preview
@Composable
fun Preview2(){
    MovieAccount()
}