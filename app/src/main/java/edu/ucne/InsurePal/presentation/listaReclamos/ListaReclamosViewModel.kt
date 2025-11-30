package edu.ucne.InsurePal.presentation.listaReclamos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.InsurePal.data.Resource
import edu.ucne.InsurePal.data.local.UserPreferences
import edu.ucne.InsurePal.domain.reclamoVehiculo.model.ReclamoVehiculo
import edu.ucne.InsurePal.domain.reclamoVehiculo.useCases.GetReclamoVehiculosUseCase
import edu.ucne.InsurePal.domain.reclamoVida.model.ReclamoVida
import edu.ucne.InsurePal.domain.reclamoVida.useCases.GetReclamosVidaUseCase
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
    private val getReclamosVidaUseCase: GetReclamosVidaUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(ListaReclamosUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val idUsuario = userPreferences.userId.first() ?: 0
            _state.update { it.copy(usuarioId = idUsuario) }

            cargarDatos(idUsuario)
        }
    }

    fun onEvent(event: ListaReclamosEvent) {
        when (event) {
            ListaReclamosEvent.OnCargarReclamos -> cargarDatos()
            ListaReclamosEvent.OnErrorDismiss -> _state.update { it.copy(error = null) }
            is ListaReclamosEvent.OnReclamoClick -> {
            }
        }
    }

    private fun cargarDatos(overrideId: Int? = null) {
        viewModelScope.launch {
            // Usamos el ID pasado por parámetro si existe, si no, usamos el del estado
            val userIdToUse = overrideId ?: _state.value.usuarioId

            if (userIdToUse == 0) {
                _state.update { it.copy(isLoading = false, error = "ID de usuario no disponible.") }
                return@launch
            }

            _state.update { it.copy(isLoading = true, error = null) }

            val resultVehiculos = getReclamosVehiculoUseCase(userIdToUse)
            val resultVida = getReclamosVidaUseCase(userIdToUse)
            val listaVehiculos = resultVehiculos.data ?: emptyList()
            val listaVida = resultVida.data ?: emptyList()

            if (resultVehiculos is Resource.Error && resultVida is Resource.Error) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "No se pudo cargar el historial completo."
                    )
                }
            } else {
                val itemsVehiculos = listaVehiculos.map { it.toUiItem() }
                val itemsVida = listaVida.map { it.toUiItem() }

                val todos = itemsVehiculos + itemsVida

                _state.update {
                    it.copy(
                        isLoading = false,
                        reclamos = todos.sortedByDescending { r -> r.fecha }
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

    private fun ReclamoVida.toUiItem(): ReclamoUiItem {
        return ReclamoUiItem(
             id = this.id,
            polizaId = this.polizaId,
            titulo = "Deceso: ${this.nombreAsegurado}",
            fecha = this.fechaFallecimiento.take(10),
            status = this.status,
            descripcion = "Causa: ${this.causaMuerte}",
            tipo = TipoReclamo.VIDA
        )
    }
}