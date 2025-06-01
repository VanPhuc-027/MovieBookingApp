package com.example.bookingmovie.data.Booking

class BookingRepository(private val bookingDao: BookingDao) {
    fun getMonthlyRevenue(): List<MonthlyRevenue> {
        return bookingDao.getMonthlyRevenue()
    }
}
