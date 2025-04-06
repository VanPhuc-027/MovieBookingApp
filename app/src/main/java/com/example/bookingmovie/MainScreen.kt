package com.example.bookingmovie

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost

import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


//Sử dụng BottomNavigation để làm thanh mục lục điều hướng ở dưới

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)
val bottomNavItems = listOf(
    BottomNavItem("category", Icons.Default.List, "Thể loại"),
    BottomNavItem("home", Icons.Default.Home, "Phim"),
    BottomNavItem("history",Icons.Default.Home,"Lịch sử"),
    BottomNavItem("settings",Icons.Default.Settings,"Tài khoản")
)

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomNavigation(
        backgroundColor = Color.DarkGray, // Màu nền thanh mục lục
        contentColor = Color.White         //Màu nền chữ trên mục lục
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        bottomNavItems.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                selectedContentColor = Color.Black, //Màu mục khi được chọn
                unselectedContentColor = Color.Gray //Màu của những mục không được chọn
            )
        }
    }
}
@Composable
fun MainScreen() {
    val navController= rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "home", //route đầu tiên được hiện lên màn hình
            modifier = Modifier.padding(padding)
        ) {
            composable("home")
            {
                MovieList() //điều hướng từ MovieList.kt
            }
            composable("category")
            {
                MovieCategory()
            }
            composable("history")
            {
                BookingHistory()
            }
            composable("settings"){
                MovieAccount()
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GrettingPreview(){
    MainScreen()
}

