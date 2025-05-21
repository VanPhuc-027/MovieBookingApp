package com.example.bookingmovie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookingmovie.Admin.MainScreen
import com.example.bookingmovie.NomalUser.UserMainScreen
import com.example.bookingmovie.ui.screens.RegisterScreen
import com.example.bookingmovie.data.AppDatabase

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
    NavHost(
        navController = navController,
        startDestination = "mainUser"
    ){
        composable("login") { LoginScreen(navController)  }
        composable("main"){ MainScreen(appNavController = navController) }
        composable("register"){ RegisterScreen(navController) }
        composable("mainUser") {UserMainScreen(appNavController = navController, itemDao = itemDao, seatDao = seatDao,roomDao = roomDao) }

    }
}

