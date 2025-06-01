package com.example.bookingmovie.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingmovie.data.AppDatabase
import com.example.bookingmovie.data.Movie.MovieEntity
import com.example.bookingmovie.data.Movie.MovieWithGenre
import com.example.bookingmovie.data.MovieGenreCrossRef.MovieGenreCrossRefDao
import com.example.bookingmovie.data.MovieGenreCrossRef.MovieGenreCrossRefEntity
import com.example.bookingmovie.data.Seat.SeatEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MovieViewModel (application: Application) : AndroidViewModel(application){
    private val movieDao = AppDatabase.getDatabase(application).movieDao()

    private val _movies = MutableStateFlow<List<MovieEntity>>(emptyList())

    private val movieGenreCrossRefDao = AppDatabase.getDatabase(application).movieGenreCrossRefDao()


    val movies : StateFlow<List<MovieEntity>> = _movies.asStateFlow()

    val allMoviesWithGenre: StateFlow<List<MovieWithGenre>> = movieDao.getAllMoviesWithGenre()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        LoadAllMovies()
    }

    private fun LoadAllMovies(){
        viewModelScope.launch {
            movieDao.getAllMoviesWithGenre().collect {
                _movies.value = it.map { movieWithGenre -> movieWithGenre.movie }
            }
        }
    }

    private fun InsertMovies(movie: MovieEntity){
        viewModelScope.launch {
            movieDao.insertMovie(movie)
        }
    }

    fun updateMovieWithGenres(movie: MovieEntity, genreIds: List<Int>) {
        viewModelScope.launch {
            movieDao.updateMovie(movie)
            movieGenreCrossRefDao.deleteCrossRefsByMovieId(movie.movie_id)
            genreIds.forEach { genreId ->
                movieGenreCrossRefDao.insertCrossRef(
                    MovieGenreCrossRefEntity(movieId = movie.movie_id, genreId = genreId)
                )
            }
        }
    }

    fun DeleteMovies(movieWithGenre: MovieWithGenre){
        viewModelScope.launch {
            movieGenreCrossRefDao.deleteCrossRefsByMovieId((movieWithGenre.movie.movie_id))
            movieDao.deleteMovies(movieWithGenre.movie)
        }
    }
    fun addMovieWithGenres(movie: MovieEntity, genreIds: List<Int>) {
        viewModelScope.launch {
            val movieId = movieDao.insertMovie(movie)
            val movieWithId = movie.copy(movie_id = movieId)

            genreIds.forEach { genreId ->
                movieGenreCrossRefDao.insertCrossRef(MovieGenreCrossRefEntity(movieId = movieId, genreId = genreId))
            }

            val db = AppDatabase.getDatabase(getApplication())
            val roomDao = db.roomDao()
            val showtimeDao = db.showtimeDao()
            val seatDao = db.seatDao()

            val rooms = roomDao.getAllRoomsOnce()
            val timeRange = listOf("09h-11h", "13h-15h", "17h-19h", "20h-22h")

            val allSeats = mutableListOf<com.example.bookingmovie.data.Seat.SeatEntity>()
            for (room in rooms) {
                for (time in timeRange) {
                    val existingShowtime = showtimeDao.getAllShowtimesOnce()
                        .find { it.movieId == movieId && it.roomId == room.room_id && it.showTime == time }
                    val showtimeId = if (existingShowtime != null) {
                        existingShowtime.showtimeId
                    } else {
                        showtimeDao.insertShowtime(
                            com.example.bookingmovie.data.Showtime.ShowtimeEntity(
                                movieId = movieId,
                                roomId = room.room_id,
                                showTime = time,
                                showDate = "2025-05-029"
                            )
                        ).toInt()
                    }

                    val seats = generateDefaultSeatsForRoomAndShowtime(room, showtimeId)
                    allSeats.addAll(seats)
                }
            }
            seatDao.insertSeats(allSeats)
        }
    }
    private fun generateDefaultSeatsForRoomAndShowtime(
        room: com.example.bookingmovie.data.Room.RoomEntity,
        showtimeId: Int
    ): List<com.example.bookingmovie.data.Seat.SeatEntity> {
        val seats = mutableListOf<com.example.bookingmovie.data.Seat.SeatEntity>()
        for (row in 'A'..'D') {
            for (num in 1..5) {
                seats.add(
                    com.example.bookingmovie.data.Seat.SeatEntity(
                        seatNumber = "$row$num",
                        isBooked = false,
                        row = row.toString(),
                        roomId = room.room_id,
                        showtimeId = showtimeId
                    )
                )
            }
        }
        return seats
    }
}