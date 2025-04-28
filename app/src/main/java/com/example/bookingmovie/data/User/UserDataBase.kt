package com.example.bookingmovie.data.User

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDataBase :RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object{
        @Volatile private var INSTANCE: UserDataBase? = null

        fun getDatabase(context: Context): UserDataBase{
            return INSTANCE ?: synchronized(this){
                Room.databaseBuilder(
                    context.applicationContext,
                    UserDataBase::class.java,
                    "user_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}