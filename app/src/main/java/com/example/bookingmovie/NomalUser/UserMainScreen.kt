package com.example.bookingmovie.NomalUser

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.History
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Filled.Home, "Trang chủ")
    object Category : BottomNavItem("category", Icons.Filled.Category, "Thể loại")
    object History : BottomNavItem("history", Icons.Outlined.History, "Lịch sử đặt")
    object Settings : BottomNavItem("settings", Icons.Filled.Person, "Tôi")
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Category,
    BottomNavItem.History,
    BottomNavItem.Settings,
)


@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (currentRoute == item.route)
                            MaterialTheme.colorScheme.primary
                        else
                            Color(0xFFB0B0B0)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        fontWeight = if (currentRoute == item.route) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (currentRoute == item.route)
                            MaterialTheme.colorScheme.primary
                        else
                            Color(0xFFB0B0B0)
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun UserMainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(BottomNavItem.Home.route) {
                UserMovieList(
                    onMovieClick = {movie ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("movie",movie)
                        navController.navigate("movie_detail")
                    }
                )
            }
            composable(BottomNavItem.Category.route) {
                MovieCategoryScreen()
            }
            composable(BottomNavItem.History.route) {
                BookingHistoryScreen()
            }
            composable(BottomNavItem.Settings.route) {
                UserAccount()
            }

            composable("movie_detail"){
                val movie = navController.previousBackStackEntry?.savedStateHandle?.get<Movie>("movie")
                movie?.let {
                    MovieDetailScreen(
                        movie = it,
                        onBack = {navController.popBackStack()},
                        onBook = {}
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    UserMainScreen()
}