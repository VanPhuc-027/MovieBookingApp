package com.example.bookingmovie.Admin

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookingmovie.ViewModels.BookingViewModel
import com.example.bookingmovie.data.AppDatabase
import com.example.bookingmovie.data.Booking.BookingDao
import com.example.bookingmovie.data.Seat.SeatDao


sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Category : BottomNavItem("category", Icons.Default.List, "Thể loại")
    object FoodDrink : BottomNavItem("menu", Icons.Default.Fastfood, "Menu")
    object Home : BottomNavItem("home", Icons.Default.Home, "Phim")
    object History : BottomNavItem("history", Icons.Default.Home, "Lịch sử")
    object Settings : BottomNavItem("settings", Icons.Default.Settings, "Tài khoản")
}

val bottomNavItems = listOf(
    BottomNavItem.Category,
    BottomNavItem.FoodDrink,
    BottomNavItem.Home,
    BottomNavItem.History,
    BottomNavItem.Settings
)
class BookingViewModelFactory(
    private val bookingDao: BookingDao,
    private val seatDao: SeatDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookingViewModel(bookingDao, seatDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    BottomNavigation(
        backgroundColor = Color(0xFF121212),
        contentColor = Color.White,
        elevation = 8.dp
    ) {
        bottomNavItems.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
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
                selectedContentColor = Color.Cyan,
                unselectedContentColor = Color.Gray
            )
        }
    }
}

@Composable
fun MainScreen(appNavController: NavHostController) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val bookingDao = AppDatabase.getDatabase(context).bookingDao()
    val seatDao = AppDatabase.getDatabase(context).seatDao()
    val bookingViewModel: BookingViewModel = viewModel(
        factory = BookingViewModelFactory(
            bookingDao = bookingDao,
            seatDao = seatDao
        )
    )
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        modifier = Modifier.padding(bottom = 4.dp)
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(BottomNavItem.Home.route) {
                MovieList()
            }
            composable(BottomNavItem.Category.route) {
                MovieCategory()
            }
            composable(BottomNavItem.History.route) {
                BookingHistory(bookingViewModel = bookingViewModel)
            }
            composable(BottomNavItem.Settings.route) {
                Account(appNavController)
            }
            composable(BottomNavItem.FoodDrink.route){
                FoodDrinkScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    val navController = rememberNavController()
    MainScreen(appNavController = navController)
}