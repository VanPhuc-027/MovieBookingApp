package com.example.bookingmovie.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bookingmovie.data.Booking.BookingDao
import com.example.bookingmovie.data.Booking.BookingEntity
import com.example.bookingmovie.data.Genre.GenreDao
import com.example.bookingmovie.data.Genre.GenreEntity
import com.example.bookingmovie.data.Item.ItemDao
import com.example.bookingmovie.data.Item.ItemEntity
import com.example.bookingmovie.data.Room.RoomDao
import com.example.bookingmovie.data.Room.RoomEntity
import com.example.bookingmovie.data.Seat.SeatDao
import com.example.bookingmovie.data.Seat.SeatEntity
import com.example.bookingmovie.data.Movie.MovieDao
import com.example.bookingmovie.data.Movie.MovieEntity
import com.example.bookingmovie.data.MovieGenreCrossRef.MovieGenreCrossRefDao
import com.example.bookingmovie.data.MovieGenreCrossRef.MovieGenreCrossRefEntity
import com.example.bookingmovie.data.Showtime.ShowtimeDao
import com.example.bookingmovie.data.Showtime.ShowtimeEntity
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
        RoomEntity::class,
        BookingEntity::class,
        ShowtimeEntity::class
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
    abstract fun bookingDao(): BookingDao
    abstract fun showtimeDao(): ShowtimeDao
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

                                val existingStaff = userDao.getUserByUsername("staff")
                                if (existingStaff == null) {
                                    val staff = UserEntity(
                                        username = "staff",
                                        gmail = "staff@gmail.com",
                                        phone_number = 0,
                                        password = "staff",
                                        role = "staff"
                                    )
                                    userDao.insertUser(staff)
                                }
                            }
                        }
                    }
                }

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "booking_movie_db"
                ).addCallback(callback)
                    .build().also { db ->
                        INSTANCE = db
                        seedData(db)
                    }

                instance
            }
        }

        private fun seedData(db: AppDatabase) {
            CoroutineScope(Dispatchers.IO).launch {
                val roomDao = db.roomDao()
                val seatDao = db.seatDao()
                val showtimeDao = db.showtimeDao()

                val currentRooms = roomDao.getAllRoomsOnce()

                if (currentRooms.isEmpty()) {
                    val rooms = generateDefaultRooms()
                    roomDao.insertRooms(rooms)

                    val insertedRooms = roomDao.getAllRoomsOnce()
                    val allShowtimes = showtimeDao.getAllShowtimesOnce()
                    val showtimeIds = allShowtimes.map { it.showtimeId }

                    //val allSeats = generateDefaultSeatsForRooms(insertedRooms,showtimeIds)
                    //seatDao.insertSeats(allSeats)
                }
            }
        }
//        private fun generateDefaultSeatsForRooms(rooms: List<RoomEntity>, showtimeIds: List<Int>): List<SeatEntity> {
//            val seats = mutableListOf<SeatEntity>()
//            for (room in rooms) {
//                for (showtimeId in showtimeIds) {
//                    for (row in 'A'..'D') {
//                        for (num in 1..5) {
//                            seats.add(
//                                SeatEntity(
//                                    seatNumber = "$row$num",
//                                    isBooked = false,
//                                    row = row.toString(),
//                                    roomId = room.room_id,
//                                    showtimeId = showtimeId
//                                )
//                            )
//                        }
//                    }
//                }
//            }
//            return seats
//        }

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
