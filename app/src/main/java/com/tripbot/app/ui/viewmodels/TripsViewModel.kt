package com.tripbot.app.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tripbot.app.data.api.RetrofitInstance
import com.tripbot.app.data.models.Trip
import kotlinx.coroutines.launch

class TripsViewModel : ViewModel() {
    private val _trips = mutableStateOf<List<Trip>>(emptyList())
    val trips: State<List<Trip>> = _trips

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    init {
        loadTrips()
    }

    fun loadTrips(userId: Int = 459015475) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = RetrofitInstance.api.getTrips(userId)
                _trips.value = result
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
