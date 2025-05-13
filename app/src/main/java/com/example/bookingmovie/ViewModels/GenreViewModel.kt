package com.example.bookingmovie.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingmovie.data.AppDatabase
import com.example.bookingmovie.data.Genre.GenreEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GenreViewModel(application: Application) : AndroidViewModel(application){
    private val genreDao = AppDatabase.getDatabase(application).genreDao()

    var genres = genreDao.getAllGenres().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun addGenre(name:String,description:String){
        viewModelScope.launch {
            val newGenre = GenreEntity(genre_name = name, genre_descripsion = description)
            genreDao.inserGenre(newGenre)
        }
    }

    fun deleteGenre(genre:GenreEntity){
        viewModelScope.launch {
            genreDao.deleteGenre(genre)
        }
    }

    fun updateGenre(genre:GenreEntity){
        viewModelScope.launch {
            genreDao.inserGenre(genre)
        }
    }

}