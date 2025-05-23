package com.example.bookingmovie.NomalUser

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bookingmovie.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun UserAccount(appNavController: NavHostController) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    Scaffold(
        containerColor = Color(0xFF0D0D0D),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Tài khoản",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Purple40
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            MenuItem(icon = Icons.Default.Favorite, text = "Yêu thích") { /* TODO */ }
            MenuItem(icon = Icons.Default.List, text = "Danh sách") { /* TODO */ }
            MenuItem(icon = Icons.Default.Person, text = "Thông tin cá nhân") { /* TODO */ }

            Spacer(modifier = Modifier.height(16.dp))

            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF292929),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            MenuItem(icon = Icons.Default.ExitToApp, text = "Thoát") {
                showLogoutDialog = true
            }
        }

        // Dialog xác nhận đăng xuất
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Xác nhận đăng xuất") },
                text = { Text("Bạn có chắc muốn đăng xuất?") },
                confirmButton = {
                    TextButton(onClick = {
                        showLogoutDialog = false
                        appNavController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }) {
                        Text("Đồng ý")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Huỷ")
                    }
                }
            )
        }
    }
}

@Composable
fun MenuItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}



@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewMovieAccount() {
    val navController = rememberNavController()
    UserAccount(appNavController = navController)
}