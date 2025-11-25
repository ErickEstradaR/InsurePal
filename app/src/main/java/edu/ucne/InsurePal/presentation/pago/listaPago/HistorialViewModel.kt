package edu.ucne.InsurePal.presentation.pago.listaPago

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.InsurePal.data.local.UserPreferences
import edu.ucne.InsurePal.domain.pago.repository.PagoRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class HistorialViewModel @Inject constructor(
    private val repository: PagoRepository,
    private val userPreferences: UserPreferences,
) : ViewModel() {

    private val _state = MutableStateFlow(HistorialPagoUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferences.userId.collectLatest { id ->

                if (id != null && id > 0) {
                    _state.update { it.copy(usuarioId = id) }

                    obtenerHistorial(id)

                    sincronizar()
                }
            }
        }
    }

    private fun obtenerHistorial(userId: Int) {
        viewModelScope.launch {

            repository.getHistorialPagos(userId)
                .catch { e ->
                    _state.update { it.copy(error = e.message ?: "Error desconocido") }
                }
                .collect { listaPagos ->
                    _state.update { it.copy(pagos = listaPagos) }
                }
        }
    }

    fun sincronizar() {
        val userId = _state.value.usuarioId

        if (userId == 0) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                repository.sincronizarPagos(userId)

                _state.update { it.copy(isLoading = false, error = null) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(isLoading = false, error = "Sin conexión para sincronizar")
                }
            }
        }
    }
}