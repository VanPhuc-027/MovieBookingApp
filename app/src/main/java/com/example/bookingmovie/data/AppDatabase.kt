package com.example.bookingmovie.data

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bookingmovie.data.Genre.GenreDao
import com.example.bookingmovie.data.Genre.GenreEntity
import com.example.bookingmovie.data.Item.ItemDao
import com.example.bookingmovie.data.Item.ItemEntity
import com.example.bookingmovie.data.Movie.MovieDao
import com.example.bookingmovie.data.Movie.MovieEntity
import com.example.bookingmovie.data.MovieGenreCrossRef.MovieGenreCrossRefDao
import com.example.bookingmovie.data.MovieGenreCrossRef.MovieGenreCrossRefEntity
import com.example.bookingmovie.data.User.UserDao
import com.example.bookingmovie.data.User.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        MovieEntity::class,
        GenreEntity::class,
        UserEntity::class,
        MovieGenreCrossRefEntity::class,
        ItemEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun genreDao(): GenreDao
    abstract fun userDao(): UserDao
    abstract fun movieGenreCrossRefDao(): MovieGenreCrossRefDao
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val callback = object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            val database = INSTANCE ?: return@launch
                            val userDao = database.userDao()
                            val existingAdmin = userDao.getUserByUsername("admin")
                            if (existingAdmin == null) {
                                val admin = UserEntity(
                                    username = "admin",
                                    gmail = "admin@gmail.com",
                                    phone_number = 0,
                                    password = "admin",
                                    role = "admin"
                                )
                                userDao.insertUser(admin)
                            }
                        }
                    }
                }

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "booking_movie_db"
                ).addCallback(callback)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
