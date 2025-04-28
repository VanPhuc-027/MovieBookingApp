package com.example.bookingmovie.ViewModels

import android.app.Application
import androidx.compose.ui.semantics.Role
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookingmovie.data.User.UserDataBase
import com.example.bookingmovie.data.User.UserEntity
import kotlinx.coroutines.launch


class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = UserDataBase.getDatabase(application).userDao()

    fun registerUser(username: String,gmail: String,phone: Int,password: String,role: String ="user"){
        viewModelScope.launch {
            val newUser = UserEntity(
                username = username,
                gmail = gmail,
                phone_number = phone,
                password = password,
                role = role
            )
            userDao.insertUser(newUser)
        }
    }
    fun clearAllUsers() {
        viewModelScope.launch {
            userDao.deleteAllUsers()
        }
    }
}