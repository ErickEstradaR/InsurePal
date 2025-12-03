package edu.ucne.InsurePal.presentation.adminListaVehiculos

import edu.ucne.InsurePal.domain.polizas.vehiculo.model.SeguroVehiculo

sealed interface ListaVehiculoEvent {
    data class OnSearchQueryChange(val query: String) : ListaVehiculoEvent
    data class OnSelectVehicle(val vehicle: SeguroVehiculo) : ListaVehiculoEvent
    data class OnUpdateStatus(val vehicle: SeguroVehiculo, val newStatus: String) : ListaVehiculoEvent
    data object OnDismissDetail : ListaVehiculoEvent
    data object Refresh : ListaVehiculoEvent
    data object OnTogglePendingFilter : ListaVehiculoEvent
}