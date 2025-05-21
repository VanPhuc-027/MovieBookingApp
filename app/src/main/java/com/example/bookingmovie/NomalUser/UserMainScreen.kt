package com.example.bookingmovie.NomalUser

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.History
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookingmovie.MovieUI.Movie.MovieUIModel
import com.example.bookingmovie.data.Booking.BookingEntity
import com.example.bookingmovie.data.Item.ItemDao
import com.example.bookingmovie.data.Room.RoomDao
import com.example.bookingmovie.data.Room.RoomEntity
import com.example.bookingmovie.data.Seat.SeatDao
import com.example.bookingmovie.data.Seat.SeatEntity

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
fun UserMainScreen(appNavController: NavHostController,itemDao: ItemDao,seatDao: SeatDao,roomDao: RoomDao) {
    val innerNavController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(innerNavController) }
    ) { padding ->
        NavHost(
            navController = innerNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(BottomNavItem.Home.route) {
                UserMovieList(appNavController = innerNavController)
            }
            composable(BottomNavItem.Category.route) {
                MovieCategoryScreen()
            }
            composable(BottomNavItem.History.route) {
                BookingHistoryScreen()
            }
            composable(BottomNavItem.Settings.route) {
                UserAccount(appNavController = appNavController)
            }

            composable("search") { SearchScreen(appNavController = innerNavController) }

            composable("movie_detail") {
                val movie: MovieUIModel? = innerNavController
                    .previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<MovieUIModel>("movie")
                movie?.let {
                    MovieDetailScreen(
                        movie = it,
                        onBack = { innerNavController.popBackStack() },
                        onBook = {
                            innerNavController.currentBackStackEntry?.savedStateHandle?.set("selectedMovie", movie)
                            innerNavController.navigate("booking/${movie.movie_id}")
                        }
                    )
                }
            }
            composable("booking/{movieId}") { backStackEntry ->
                val movie = innerNavController
                    .previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<MovieUIModel>("selectedMovie")

                movie?.let {
                    val allRooms by roomDao.getAllRooms().collectAsState(initial = emptyList())
                    val selectedRoomId = allRooms.firstOrNull()?.room_id ?: 0
                    val seats by seatDao.getSeatsByRoomId(selectedRoomId).collectAsState(initial = emptyList())


                    val foodItems by itemDao.getAllItems().collectAsState(initial = emptyList())

                    val insertBooking = { booking: BookingEntity ->
                        println("Booking đã được lưu: $booking")
                    }

                    BookingScreen(
                        movie = it,
                        onBack = { innerNavController.popBackStack() },
                        onBookingSuccess = { innerNavController.popBackStack() },
                        roomList = allRooms,
                        seatListProvider = { seats },
                        insertBooking = insertBooking,
                        foodItems = foodItems,
                        seatDao = seatDao
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    val navController = rememberNavController()
    //UserMainScreen(appNavController = navController as NavHostController)
}