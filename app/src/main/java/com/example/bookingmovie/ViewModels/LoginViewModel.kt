package com.example.bookingmovie.ViewModels

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingmovie.data.AppDatabase
import com.example.bookingmovie.data.User.UserEntity
import com.example.bookingmovie.data.User.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    internal val repo = UserRepository(db.userDao())

    var username by mutableStateOf("")
    var gmail by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var password by mutableStateOf("")
    var role by mutableStateOf("User")  // hoặc để mặc định là "User"

    fun insertUser() {
        viewModelScope.launch {
            val user = UserEntity(
                username = username,
                gmail = gmail,
                phone_number = phoneNumber.toIntOrNull() ?: 0,
                password = password,
                role = role
            )
            repo.insertUser(user)
        }
    }

    fun loginUser(
        inputUsername: String,
        inputPassword: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            val user = repo.getUserByUsername(inputUsername)
            if (user != null && user.password == inputPassword) {
                onSuccess()
            } else {
                onError()
            }
        }
    }
    fun ensureAdminExists() {
        viewModelScope.launch {
            val existing = repo.getUserByUsername("admin")
            if (existing == null) {
                val admin = UserEntity(
                    username = "admin",
                    gmail = "admin@gmail.com",
                    phone_number = 123456789,
                    password = "admin",
                    role = "admin"
                )
                repo.insertUser(admin)
            }
        }
    }
}
