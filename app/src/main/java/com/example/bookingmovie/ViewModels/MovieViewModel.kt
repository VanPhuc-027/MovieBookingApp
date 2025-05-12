package com.example.bookingmovie.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingmovie.data.AppDatabase
import com.example.bookingmovie.data.Movie.MovieEntity
import com.example.bookingmovie.data.Movie.MovieWithGenre
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MovieViewModel (application: Application) : AndroidViewModel(application){
    private val movieDao = AppDatabase.getDatabase(application).movieDao()

    private val _movies = MutableStateFlow<List<MovieEntity>>(emptyList())
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

    /*private fun UpdateMovies(movie: MovieEntity){
        viewModelScope.launch {
            movieDao.udateMovie(movie)
        }
    }*/

    private fun DeleteMovies(movie: MovieEntity){
        viewModelScope.launch {
            movieDao.deleteMovies(movie)
        }
    }

}