package com.example.bookingmovie.data

import com.example.bookingmovie.data.Movie.MovieDao
import com.example.bookingmovie.data.Movie.MovieEntity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookingmovie.data.Genre.GenreDao
import com.example.bookingmovie.data.Genre.GenreEntity
import com.example.bookingmovie.data.Item.ItemDao
import com.example.bookingmovie.data.Item.ItemEntity
import com.example.bookingmovie.data.User.UserDao
import com.example.bookingmovie.data.User.UserEntity

@Database(
    entities = [
        MovieEntity::class,
        GenreEntity::class,
        UserEntity::class,
        ItemEntity::class
               ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun genreDao(): GenreDao
    abstract fun userDao() : UserDao
    abstract fun itemDao() : ItemDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "booking_movie_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}