package edu.ucne.InsurePal.data.remote.reclamoVehiculo

data class ReclamoUpdateRequest(

    val status: String,

    val motivoRechazo: String?
)