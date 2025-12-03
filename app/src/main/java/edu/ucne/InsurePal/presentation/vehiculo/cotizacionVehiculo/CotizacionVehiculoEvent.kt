package edu.ucne.InsurePal.presentation.vehiculo.cotizacionVehiculo

sealed interface CotizacionVehiculoEvent {
    data object OnContinuarPagoClick : CotizacionVehiculoEvent
    data object OnVolverClick : CotizacionVehiculoEvent
}