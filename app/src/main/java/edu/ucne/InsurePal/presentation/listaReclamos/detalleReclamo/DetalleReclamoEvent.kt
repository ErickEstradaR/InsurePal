package edu.ucne.InsurePal.presentation.listaReclamos.detalleReclamo

sealed interface DetalleReclamoEvent {
    data object OnErrorDismiss : DetalleReclamoEvent
    data object OnReintentar : DetalleReclamoEvent
}