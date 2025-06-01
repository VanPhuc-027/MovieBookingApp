    package com.example.bookingmovie.data.User

    class UserRepository(private val userDao: UserDao) {
        suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)


        suspend fun getUserByUsername(username: String): UserEntity? {
            return userDao.getUserByUsername(username)
        }

        suspend fun checkLogin(inputUsername: String, inputPassword: String): Boolean {
            val user = userDao.login(inputUsername, inputPassword)
            return user != null
        }
    }
