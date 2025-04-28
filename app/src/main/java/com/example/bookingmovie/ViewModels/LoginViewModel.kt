package com.example.bookingmovie.ViewModels

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingmovie.data.User.UserDataBase
import com.example.bookingmovie.data.User.UserEntity
import com.example.bookingmovie.data.User.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val db = UserDataBase.getDatabase(application)
    private val repo = UserRepository(db.userDao())

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
}
