package com.example.bookingmovie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookingmovie.Admin.BookingViewModelFactory
import com.example.bookingmovie.Admin.MainScreen
import com.example.bookingmovie.NomalUser.UserMainScreen
import com.example.bookingmovie.Staff.StaffMainScreen
import com.example.bookingmovie.Staff.StaffQrScanScreen
import com.example.bookingmovie.ViewModels.LoginViewModel
import com.example.bookingmovie.ui.screens.RegisterScreen
import com.example.bookingmovie.data.AppDatabase
import com.example.bookingmovie.data.User.UserEntity


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppBooking()
        }
    }
}

@Composable
fun MovieAppBooking(){
    val navController = rememberNavController()
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val itemDao = db.itemDao()
    val roomDao = db.roomDao()
    val seatDao = db.seatDao()
    val showtimeDao = db.showtimeDao()
    val loginViewModel: LoginViewModel =   viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ){
        composable("login") { LoginScreen(navController,loginViewModel)  }
        composable("main"){ MainScreen(appNavController = navController) }
        composable("register"){ RegisterScreen(navController) }
        composable("staff") { StaffMainScreen(navController) }
        composable("mainUser/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
            userId?.let { id ->
                val userState = androidx.compose.runtime.produceState<com.example.bookingmovie.data.User.UserEntity?>(initialValue = null, id) {
                    value = db.userDao().getUserById(id)
                }
                val user = userState.value
                if (user != null) {
                    UserMainScreen(
                        appNavController = navController,
                        itemDao = itemDao,
                        seatDao = seatDao,
                        roomDao = roomDao,
                        currentUser = user,
                        showtimeDao = showtimeDao,
                        bookingViewModel = viewModel(
                            factory = BookingViewModelFactory(
                                bookingDao = db.bookingDao(),
                                seatDao = seatDao
                            )
                    )
                    )
                } else {
                }
            } ?: navController.navigate("login")
        }
    }
}

