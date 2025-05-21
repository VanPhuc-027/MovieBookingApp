package com.example.bookingmovie.data

import com.example.bookingmovie.data.Movie.MovieDao
import com.example.bookingmovie.data.Movie.MovieEntity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bookingmovie.data.Genre.GenreDao
import com.example.bookingmovie.data.Genre.GenreEntity
import com.example.bookingmovie.data.MovieGenreCrossRef.MovieGenreCrossRefDao
import com.example.bookingmovie.data.MovieGenreCrossRef.MovieGenreCrossRefEntity
import com.example.bookingmovie.data.Item.ItemDao
import com.example.bookingmovie.data.Item.ItemEntity
import com.example.bookingmovie.data.Room.RoomDao
import com.example.bookingmovie.data.Room.RoomEntity
import com.example.bookingmovie.data.Seat.SeatDao
import com.example.bookingmovie.data.Seat.SeatEntity
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
        ItemEntity::class,
        SeatEntity::class,
        RoomEntity::class
               ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun genreDao(): GenreDao
    abstract fun userDao() : UserDao
    abstract fun movieGenreCrossRefDao () :MovieGenreCrossRefDao
    abstract fun itemDao() : ItemDao
    abstract fun seatDao () :SeatDao
    abstract fun roomDao () :RoomDao
    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "booking_movie_db"
                ).build().also { db ->
                    INSTANCE = db
                    // Gọi seed sau khi INSTANCE đã có
                    seedData(db)
                }
            }
        }
        private fun seedData(db: AppDatabase) {
            CoroutineScope(Dispatchers.IO).launch {
                val roomDao = db.roomDao()
                val seatDao = db.seatDao()

                val currentRooms = roomDao.getAllRoomsOnce()

                // Chỉ seed nếu chưa có phòng
                if (currentRooms.isEmpty()) {
                    val rooms = generateDefaultRooms()
                    roomDao.insertRooms(rooms)

                    // Đợi insert xong, rồi đọc lại rooms từ DB
                    val insertedRooms = roomDao.getAllRoomsOnce()

                    // Debug log để kiểm tra số lượng phòng:
                    println("Inserted rooms count: ${insertedRooms.size}")

                    val allSeats = generateDefaultSeatsForRooms(insertedRooms)
                    seatDao.insertSeats(allSeats)

                    // Debug log kiểm tra số lượng seat insert
                    println("Inserted seats count: ${allSeats.size}")
                }
            }
        }


        private fun generateDefaultSeatsForRooms(rooms: List<RoomEntity>): List<SeatEntity> {
            val seats = mutableListOf<SeatEntity>()
            for (room in rooms) {
                for (row in 'A'..'D') {
                    for (num in 1..6) {
                        seats.add(
                            SeatEntity(
                                seatNumber = "$row$num",
                                isBooked = false,
                                row = row.toString(),
                                roomId = room.room_id
                            )
                        )
                    }
                }
            }
            return seats
        }

        private fun generateDefaultRooms(): List<RoomEntity> {
            return listOf(
                RoomEntity( Room_Number = "Phòng 1"),
                RoomEntity(Room_Number = "Phòng 2"),
                RoomEntity(Room_Number = "Phòng 3"),
                RoomEntity(Room_Number = "Phòng 4")
            )
        }

    }
}