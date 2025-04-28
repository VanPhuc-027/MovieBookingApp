package com.example.bookingmovie.data.User

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)
    suspend fun login(username: String, password: String) = userDao.login(username, password)
}
