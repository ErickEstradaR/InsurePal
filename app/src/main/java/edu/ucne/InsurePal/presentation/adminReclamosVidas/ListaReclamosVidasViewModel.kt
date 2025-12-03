package edu.ucne.InsurePal.presentation.adminReclamosVidas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.InsurePal.data.Resource
import edu.ucne.InsurePal.domain.reclamoVida.model.ReclamoVida
import edu.ucne.InsurePal.domain.reclamoVida.useCases.GetReclamosVidaUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ListaReclamosVidasViewModel @Inject constructor(
    private val getReclamosUseCase: GetReclamosVidaUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ListaReclamosVidasUiState())
    val state = _state.asStateFlow()

    init {
        cargarReclamos()
    }

    fun onEvent(event: ListaReclamosVidasEvent) {
        when (event) {
            ListaReclamosVidasEvent.OnCargar -> cargarReclamos()
            is ListaReclamosVidasEvent.OnReclamoClick -> {//navega al reclamo
            }
            ListaReclamosVidasEvent.OnErrorDismiss -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    private fun cargarReclamos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = getReclamosUseCase(null)

            when (result) {
                is Resource.Success -> {
                    val lista = result.data ?: emptyList()
                    val listaOrdenada = lista.sortedWith(
                        compareBy<ReclamoVida> { it.status != "PENDIENTE" }
                            .thenByDescending { it.fechaCreacion }
                    )

                    _state.update {
                        it.copy(
                            isLoading = false,
                            reclamos = listaOrdenada
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(isLoading = false, error = result.message ?: "Error desconocido")
                    }
                }
                is Resource.Loading -> { //manejado al inicio
                 }
            }
        }
    }
}