package edu.ucne.InsurePal.presentation.admin.adminListaVehiculos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.InsurePal.data.Resource
import edu.ucne.InsurePal.domain.polizas.vehiculo.model.SeguroVehiculo
import edu.ucne.InsurePal.domain.polizas.vehiculo.useCases.GetAllVehiculosUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class VehicleListViewModel @Inject constructor(
    private val getAllVehiculosUseCase: GetAllVehiculosUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ListaVehiculoUiState())
    val state: StateFlow<ListaVehiculoUiState> = _state.asStateFlow()

    init {
        loadVehicles()
    }

    fun onEvent(event: ListaVehiculoEvent) {
        when (event) {
            is ListaVehiculoEvent.OnSearchQueryChange -> {
                _state.update {
                    it.copy(
                        searchQuery = event.query,
                        filteredVehicles = filterList(it.vehicles, event.query)
                    )
                }
            }
            is ListaVehiculoEvent.OnSelectVehicle -> {
                _state.update { it.copy(selectedVehicle = event.vehicle, isDetailVisible = true) }
            }
            ListaVehiculoEvent.OnDismissDetail -> {
                _state.update { it.copy(selectedVehicle = null, isDetailVisible = false) }
            }
            ListaVehiculoEvent.Refresh -> {
                loadVehicles()
            }
        }
    }

    private fun loadVehicles() {
        viewModelScope.launch {
            getAllVehiculosUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                    is Resource.Success -> {
                        val list = result.data ?: emptyList()
                        _state.update {
                            it.copy(
                                isLoading = false,
                                vehicles = list,
                                filteredVehicles = filterList(list, it.searchQuery)
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(isLoading = false, errorMessage = result.message) }
                    }
                }
            }
        }
    }

    private fun filterList(list: List<SeguroVehiculo>, query: String): List<SeguroVehiculo> {
        if (query.isBlank()) return list
        return list.filter {
            it.placa.contains(query, ignoreCase = true) ||
                    it.marca.contains(query, ignoreCase = true) ||
                    it.modelo.contains(query, ignoreCase = true)
        }
    }
}