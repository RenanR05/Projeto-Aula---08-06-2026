package com.univali.geolocation.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.univali.geolocation.domain.model.LocationData
import com.univali.geolocation.domain.repository.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class DashboardUiState {
    object Loading : DashboardUiState()
    data class Success(val location: LocationData) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

class DashboardViewModel(
    private val repository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        startObservingLocation()
    }

    private fun startObservingLocation() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            repository.observeLocation()
                .catch { e ->
                    _uiState.value = DashboardUiState.Error(e.message ?: "Erro desconhecido")
                }
                .collect { location ->
                    _uiState.value = DashboardUiState.Success(location)
                }
        }
    }

    fun refreshLocation() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            try {
                val location = repository.getCurrentLocation()
                _uiState.value = DashboardUiState.Success(location)
            } catch (e: Exception) {
                _uiState.value = DashboardUiState.Error(e.message ?: "Erro ao atualizar")
            }
        }
    }
}
