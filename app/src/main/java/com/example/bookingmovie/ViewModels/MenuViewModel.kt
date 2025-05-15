package com.example.bookingmovie.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingmovie.data.AppDatabase
import com.example.bookingmovie.data.Item.ItemDao
import com.example.bookingmovie.data.Item.ItemEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MenuViewModel (application: Application) : AndroidViewModel(application) {
    private val itemDao = AppDatabase.getDatabase(application).itemDao()

    val foodDrinks = itemDao.getAllItems().
    stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun addFoodDrink(name : String, price : Int, quantity : Int){
        viewModelScope.launch {
            val newItem = ItemEntity(
                name = name,
                price = price,
                quantity = quantity
            )
            itemDao.insertItem(newItem)
        }
    }

    fun deleteFoodDrink(item : ItemEntity){
        viewModelScope.launch {
            itemDao.deleteItem(item)
        }
    }

    fun updateFoodDrink(item : ItemEntity){
        viewModelScope.launch {
            itemDao.insertItem(item)
        }
    }
}