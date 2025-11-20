package edu.ucne.InsurePal.data.remote.polizas.vehiculo

import java.time.LocalDate

data class SeguroVehiculoResponse(
    val idPoliza: String? = null,
    val name: String,
    val status: String,
    val expirationDate: LocalDate,

    val placa: String,
    val modeloVehiculo: String,
    val coverageType: String
)