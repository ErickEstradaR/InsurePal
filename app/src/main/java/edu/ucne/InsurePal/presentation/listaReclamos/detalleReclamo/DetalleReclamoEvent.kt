package edu.ucne.InsurePal.presentation.listaReclamos.detalleReclamo

sealed interface DetalleReclamoEvent {
    data object OnErrorDismiss : DetalleReclamoEvent
    data object OnReintentar : DetalleReclamoEvent

    data object OnAprobar : DetalleReclamoEvent
    data class OnRechazar(val motivo: String) : DetalleReclamoEvent
}