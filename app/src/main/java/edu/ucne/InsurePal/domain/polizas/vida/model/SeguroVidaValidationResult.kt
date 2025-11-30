package edu.ucne.InsurePal.domain.polizas.vida.model

data class SeguroVidaValidationResult(
    val errorNombres: String? = null,
    val errorCedula: String? = null,
    val errorFechaNacimiento: String? = null,
    val errorOcupacion: String? = null,
    val errorMontoCobertura: String? = null,
    val errorNombreBeneficiario: String? = null,
    val errorCedulaBeneficiario: String? = null,
    val errorParentesco: String? = null
) {
    val esValido: Boolean
        get() = errorNombres == null && errorCedula == null &&
                errorFechaNacimiento == null && errorOcupacion == null &&
                errorMontoCobertura == null && errorNombreBeneficiario == null &&
                errorCedulaBeneficiario == null && errorParentesco == null
}