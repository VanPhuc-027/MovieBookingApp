package com.example.bookingmovie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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
    NavHost(
        navController = navController,
        startDestination = "main"
    ){
        composable("login") { LoginScreen(navController)  }
        composable("main"){ MainScreen() }
    }
}

