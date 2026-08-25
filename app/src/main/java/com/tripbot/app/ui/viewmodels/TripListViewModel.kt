package com.tripbot.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tripbot.app.data.models.Trip
import com.tripbot.app.data.repository.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TripListViewModel : ViewModel() {
    private val repository = TripRepository()

    private val _trips = MutableStateFlow<List<Trip>>(emptyList())
    val trips: StateFlow<List<Trip>> = _trips

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadTrips()
    }

    fun loadTrips(userId: Int = 1) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _trips.value = repository.getTrips(userId)
            } catch (e: Exception) {
                // В реальном приложении можно показать ошибку
            } finally {
                _isLoading.value = false
            }
        }
    }
}
