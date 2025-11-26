package edu.ucne.InsurePal.presentation.pago

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.InsurePal.data.Resource
import edu.ucne.InsurePal.domain.pago.model.TarjetaCredito
import edu.ucne.InsurePal.domain.pago.useCase.ProcesarPagoUseCase
import edu.ucne.InsurePal.domain.polizas.vehiculo.repository.SeguroVehiculoRepository
import edu.ucne.InsurePal.domain.polizas.vida.repository.SeguroVidaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class PagoViewModel @Inject constructor(
    private val procesarPago: ProcesarPagoUseCase,
    private val vehiculoRepository: SeguroVehiculoRepository,
    private val vidaRepository: SeguroVidaRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(PagoUiState())
    val state = _state.asStateFlow()

    private val polizaId: String = savedStateHandle.get<String>("polizaId") ?: ""
    private val monto: Double = savedStateHandle.get<Double>("monto") ?: 0.0
    private val descripcion: String = savedStateHandle.get<String>("descripcion") ?: "Pago de Póliza"

    init {
        _state.update { it.copy(polizaId = polizaId, montoAPagar = monto) }
    }

    fun onEvent(event: PagoEvent) {
        when(event) {
            is PagoEvent.OnNumeroChange -> _state.update { it.copy(numeroTarjeta = event.numero) }
            is PagoEvent.OnFechaChange -> _state.update { it.copy(fechaVencimiento = event.fecha) }
            is PagoEvent.OnCvvChange -> _state.update { it.copy(cvv = event.cvv) }
            is PagoEvent.OnTitularChange -> _state.update { it.copy(titular = event.nombre) }

            PagoEvent.OnDialogDismiss -> _state.update { it.copy(mensajeError = null) }

            PagoEvent.OnPagarClick -> realizarPagoDirecto()
        }
    }

    private fun realizarPagoDirecto() {
        val uiState = _state.value

        if (uiState.numeroTarjeta.isBlank() || uiState.cvv.isBlank()) {
            _state.update { it.copy(mensajeError = "Por favor llene todos los campos") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val tarjeta = TarjetaCredito(
                numero = uiState.numeroTarjeta,
                titular = uiState.titular,
                fechaVencimiento = uiState.fechaVencimiento,
                cvv = uiState.cvv
            )

            val result = procesarPago(
                polizaId = uiState.polizaId,
                monto = uiState.montoAPagar,
                tarjeta = tarjeta
            )

            when(result) {
                is Resource.Success -> {
                    // Si cobró, actualizamos la póliza
                    actualizarEstadoPoliza(uiState.polizaId)

                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, mensajeError = result.message) }
                }
                is Resource.Loading -> _state.update { it.copy(isLoading = true) }
            }
        }
    }

    private suspend fun actualizarEstadoPoliza(id: String) {
        try {
            val nuevaFechaPago = LocalDate.now().plusMonths(1).toString()

            if (id.startsWith("VIDA-")) {
                val resultGet = vidaRepository.getSeguroVidaById(id)
                if (resultGet is Resource.Success) {
                    val seguroVida = resultGet.data
                    if (seguroVida != null) {
                        val actualizado = seguroVida.copy(
                            esPagado = true,
                            fechaPago = nuevaFechaPago
                        )
                        vidaRepository.updateSeguroVida(id, actualizado)
                    }
                }

            } else {
                val resultGet = vehiculoRepository.getVehiculo(id)
                if (resultGet is Resource.Success) {
                    val seguroVehiculo = resultGet.data
                    if (seguroVehiculo != null) {
                        val actualizado = seguroVehiculo.copy(
                            esPagado = true,
                            fechaPago = nuevaFechaPago,
                            status = "Mensualidad Paga"
                        )
                        vehiculoRepository.putVehiculo(id, actualizado)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}