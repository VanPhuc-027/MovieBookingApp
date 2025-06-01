package com.example.bookingmovie.Admin

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.bookingmovie.ViewModels.RevenueViewModel
import com.example.bookingmovie.data.AppDatabase
import com.example.bookingmovie.data.Booking.BookingRepository


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Account(appNavController: NavHostController) {
    val showLogoutDialog = remember { mutableStateOf(false) }
    val showChangePasswordDialog = remember { mutableStateOf(false) }
    val showReportScreen = remember { mutableStateOf(false) }

    if (showReportScreen.value) {
        val context = LocalContext.current
        val viewModel = remember {
            RevenueViewModel(
                BookingRepository(
                    AppDatabase.getDatabase(context).bookingDao()
                )
            )
        }

        RevenueReportScreen(
            viewModel = viewModel,
            onBack = { showReportScreen.value = false }
        )
    } else {
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

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                AccountOptionItem(
                    icon = Icons.Default.BarChart,
                    text = "Báo cáo doanh thu",
                    onClick = { showReportScreen.value = true }
                )

                AccountOptionItem(
                    icon = Icons.Default.Lock,
                    text = "Đổi mật khẩu",
                    onClick = { showChangePasswordDialog.value = true }
                )

                AccountOptionItem(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    text = "Đăng xuất",
                    onClick = { showLogoutDialog.value = true }
                )

                if (showLogoutDialog.value) {
                    AlertDialog(
                        onDismissRequest = { showLogoutDialog.value = false },
                        title = { Text("Xác nhận đăng xuất") },
                        text = { Text("Bạn có chắc chắn muốn đăng xuất không?") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showLogoutDialog.value = false
                                    appNavController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            ) {
                                Text("Đăng xuất")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                showLogoutDialog.value = false
                            }) {
                                Text("Hủy")
                            }
                        }
                    )
                }

                if (showChangePasswordDialog.value) {
                    ChangePasswordDialog(
                        onDismiss = { showChangePasswordDialog.value = false },
                        onConfirm = { oldPass, newPass ->
                            showChangePasswordDialog.value = false
                        }
                    )
                }
            }
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

@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Đổi mật khẩu") },
        text = {
            Column {
                OutlinedTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    label = { Text("Mật khẩu cũ") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Mật khẩu mới") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(oldPassword, newPassword)
            }) {
                Text("Xác nhận")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}


