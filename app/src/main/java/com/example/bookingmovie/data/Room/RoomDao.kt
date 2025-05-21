package com.example.bookingmovie.data.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRooms(rooms: List<RoomEntity>)

    @Query("SELECT * FROM Room")
    fun getAllRooms(): Flow<List<RoomEntity>>

    @Query("SELECT * FROM Room")
    suspend fun getAllRoomsOnce(): List<RoomEntity>
}