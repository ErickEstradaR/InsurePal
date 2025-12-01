package edu.ucne.InsurePal.domain.polizas.vida.model

data class ValidateSeguroVidaParams(
    val nombres: String,
    val cedula: String,
    val fechaNacimiento: String,
    val ocupacion: String,
    val montoCobertura: String,
    val nombreBeneficiario: String,
    val cedulaBeneficiario: String,
    val parentesco: String
)