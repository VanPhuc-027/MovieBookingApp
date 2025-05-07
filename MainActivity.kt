package com.example.bookingmovie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieBookingApp()
        }
    }
}

@Composable
fun MovieBookingApp() {
    val navController = rememberNavController()
    AppNavigation(navController = navController)
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(route = Screen.Main.route) {
            MainScreen()
        }
    }
}

// Quản lý route bằng sealed class
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Main : Screen("main")
}
