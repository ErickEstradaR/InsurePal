package edu.ucne.InsurePal.presentation.listaReclamos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.InsurePal.data.Resource
import edu.ucne.InsurePal.data.local.UserPreferences
import edu.ucne.InsurePal.domain.reclamoVehiculo.model.ReclamoVehiculo
import edu.ucne.InsurePal.domain.reclamoVehiculo.useCases.GetReclamoVehiculosUseCase
import edu.ucne.InsurePal.presentation.listaReclamos.UiModels.ReclamoUiItem
import edu.ucne.InsurePal.presentation.listaReclamos.UiModels.TipoReclamo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListaReclamosViewModel @Inject constructor(
    private val getReclamosVehiculoUseCase: GetReclamoVehiculosUseCase,
    // En el futuro inyectarás aquí: private val getReclamosVidaUseCase: GetReclamosVidaUseCase
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(ListaReclamosUiState())
    val state = _state.asStateFlow()

    init {
        cargarDatos()
    }

    fun onEvent(event: ListaReclamosEvent) {
        when (event) {
            ListaReclamosEvent.OnCargarReclamos -> cargarDatos()
            ListaReclamosEvent.OnErrorDismiss -> _state.update { it.copy(error = null) }
            is ListaReclamosEvent.OnReclamoClick -> {
                // Aquí podrías navegar al detalle si lo deseas
            }
        }
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val userId = userPreferences.userId.first()

            // 1. Obtener Reclamos de Vehículos
            val resultVehiculos = getReclamosVehiculoUseCase(userId)

            // 2. (Futuro) Obtener Reclamos de Vida
            // val resultVida = getReclamosVidaUseCase(userId)

            if (resultVehiculos is Resource.Success) {
                val listaVehiculos = resultVehiculos.data ?: emptyList()

                // Mapeamos a la lista Genérica
                val itemsUi = listaVehiculos.map { it.toUiItem() }
                // + (Futuro) listaVida.map { it.toUiItem() }

                _state.update {
                    it.copy(
                        isLoading = false,
                        reclamos = itemsUi.sortedByDescending { r -> r.fecha }
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = resultVehiculos.message ?: "Error al cargar historial"
                    )
                }
            }
        }
    }

    private fun ReclamoVehiculo.toUiItem(): ReclamoUiItem {
        return ReclamoUiItem(
            id = this.id,
            polizaId = this.polizaId,
            titulo = "${this.tipoIncidente} - ${this.direccion}",
            fecha = this.fechaIncidente.take(10),
            status = this.status,
            descripcion = this.descripcion,
            tipo = TipoReclamo.VEHICULO
        )
    }
}