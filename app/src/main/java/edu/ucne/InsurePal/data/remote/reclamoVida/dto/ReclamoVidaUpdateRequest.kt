package edu.ucne.InsurePal.data.remote.reclamoVida.dto

data class ReclamoVidaUpdateRequest(
    val status: String,

    val motivoRechazo: String?
)