package com.example.bookingmovie.Staff

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.zxing.integration.android.IntentIntegrator
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.bookingmovie.Admin.BottomNavItem.Category.icon


sealed class StaffBottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object QRScan : StaffBottomNavItem(
        "staff_qr",
        "Quét mã",
        Icons.Filled.QrCodeScanner
    )

    object Profile : StaffBottomNavItem(
        "staff_profile",
        "Tài khoản",
        Icons.Filled.AccountCircle
    )
}


@Composable
fun StaffBottomNavigationBar(navController: NavController) {
    NavigationBar {
        val items = listOf(
            StaffBottomNavItem.QRScan,
            StaffBottomNavItem.Profile
        )
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(StaffBottomNavItem.QRScan.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                label = { Text(item.title) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) }
            )
        }
    }
}



@Composable
fun StaffQrScanScreen(
    onQrScanned: (String) -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val intentResult = IntentIntegrator.parseActivityResult(result.resultCode, result.data)
        if (intentResult != null && intentResult.contents != null) {
            onQrScanned(intentResult.contents)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Quét mã QR", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val integrator = IntentIntegrator(context as Activity)
            integrator.setPrompt("Đưa mã QR vào camera")
            integrator.setOrientationLocked(false)
            launcher.launch(integrator.createScanIntent())
        }) {
            Text("Bắt đầu quét")
        }
    }
}

@Composable
fun StaffMainScreen(navController: NavController) {
    val staffNavController = rememberNavController()
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            NavHost(
                navController = staffNavController,
                startDestination = StaffBottomNavItem.QRScan.route
            ) {
                composable(StaffBottomNavItem.QRScan.route) {
                    StaffQrScanScreen(
                        onQrScanned = { result ->
                            println("QR: $result")
                        }
                    )
                }
                composable(StaffBottomNavItem.Profile.route) {
                    StaffProfileScreen(navController)
                }
            }
        }
        StaffBottomNavigationBar(navController = staffNavController)
    }
}


@Preview(showBackground = true)
@Composable
fun QRPreview (){
    //StaffQrScanScreen {  }
}
