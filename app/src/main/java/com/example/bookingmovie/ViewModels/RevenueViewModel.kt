package com.example.bookingmovie.ViewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingmovie.data.Booking.BookingRepository
import com.example.bookingmovie.data.Booking.MonthlyRevenue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RevenueViewModel(private val repository: BookingRepository) : ViewModel() {

    private val _revenueList = mutableStateOf<List<MonthlyRevenue>>(emptyList())
    val revenueList: State<List<MonthlyRevenue>> = _revenueList

    fun loadRevenue() {
        viewModelScope.launch {
            _revenueList.value = withContext(Dispatchers.IO) {
                repository.getMonthlyRevenue()
            }
        }
    }
}
