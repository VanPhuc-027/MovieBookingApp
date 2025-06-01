package com.example.bookingmovie.NomalUser

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.History
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookingmovie.MovieUI.Movie.MovieUIModel
import com.example.bookingmovie.ViewModels.BookingViewModel
import com.example.bookingmovie.data.AppDatabase
import com.example.bookingmovie.data.Booking.BookingDao
import com.example.bookingmovie.data.Booking.BookingEntity
import com.example.bookingmovie.data.Item.ItemDao
import com.example.bookingmovie.data.Room.RoomDao
import com.example.bookingmovie.data.Room.RoomEntity
import com.example.bookingmovie.data.Seat.SeatDao
import com.example.bookingmovie.data.Seat.SeatEntity
import com.example.bookingmovie.data.Showtime.ShowtimeDao
import com.example.bookingmovie.data.User.UserEntity

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Filled.Home, "Trang chủ")
    object History : BottomNavItem("history", Icons.Outlined.History, "Lịch sử đặt")
    object Settings : BottomNavItem("settings", Icons.Filled.Person, "Tài khoản")
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.History,
    BottomNavItem.Settings,
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar(
        containerColor = Color(0xFF0D1B2A),
        tonalElevation = 0.dp,
        modifier = Modifier.height(70.dp)
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
fun UserMainScreen(bookingViewModel: BookingViewModel,appNavController: NavHostController,itemDao: ItemDao,seatDao: SeatDao,roomDao: RoomDao,currentUser: UserEntity,showtimeDao: ShowtimeDao) {
    LaunchedEffect(Unit) {
        bookingViewModel.checkAndUpdateExpiredBookings()
    }
    val innerNavController = rememberNavController()
    val context = LocalContext.current
    val bookingDao = AppDatabase.getDatabase(context).bookingDao()
    val bookingViewModel: BookingViewModel = viewModel(
        factory = BookingViewModelFactory(
            bookingDao = bookingDao,
            seatDao = seatDao
        )
    )
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
            composable(BottomNavItem.History.route) {
                BookingHistoryScreen(
                    bookingViewModel = bookingViewModel,
                    currentUserId = currentUser.user_id
                )
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
                    val allSeats by seatDao.getAllSeats().collectAsState(initial = emptyList())
                    val showtimes by showtimeDao.getShowtimesByMovieId(movie.movie_id).collectAsState(initial = emptyList())
                    val bookingDao = AppDatabase.getDatabase(LocalContext.current).bookingDao()

                    val foodItems by itemDao.getAllItems().collectAsState(initial = emptyList())
                    BookingScreen(
                        movie = it,
                        currentUser = currentUser,
                        onBack = { innerNavController.popBackStack() },
                        onBookingSuccess = { innerNavController.popBackStack() },
                        roomList = allRooms,
                        seatListProvider = { roomId, showtimeId ->
                            allSeats.filter { it.roomId == roomId && it.showtimeId == showtimeId }
                        },
                        foodItems = foodItems,
                        showtimes = showtimes,
                        viewModel = bookingViewModel
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