package edu.ucne.InsurePal.domain.polizas.vida.useCases

import edu.ucne.InsurePal.domain.polizas.vida.model.SeguroVidaValidationResult
import edu.ucne.InsurePal.domain.polizas.vida.model.ValidateSeguroVidaParams
import jakarta.inject.Inject

class ValidateSeguroVidaUseCase @Inject constructor() {

    operator fun invoke(params: ValidateSeguroVidaParams): SeguroVidaValidationResult {
        return with(params) {
            SeguroVidaValidationResult(
                errorNombres = validateRequired(nombres),
                errorCedula = validateCedula(cedula),
                errorFechaNacimiento = validateRequired(fechaNacimiento, "Requerida"),
                errorOcupacion = validateRequired(ocupacion, "Seleccione una ocupación"),
                errorMontoCobertura = validateMonto(montoCobertura),
                errorNombreBeneficiario = validateRequired(nombreBeneficiario),
                errorCedulaBeneficiario = validateCedula(cedulaBeneficiario),
                errorParentesco = validateRequired(parentesco)
            )
        }
    }
    
    private fun validateRequired(value: String, message: String = "Requerido"): String? {
        return if (value.isBlank()) message else null
    }

    private fun validateCedula(cedula: String): String? {
        val isValid = cedula.length == 11 && cedula.all { it.isDigit() }
        return if (!isValid) "Debe tener 11 dígitos" else null
    }

    private fun validateMonto(montoText: String): String? {
        val montoVal = montoText.toDoubleOrNull() ?: 0.0
        return when {
            montoVal <= 0 -> "Monto inválido"
            montoVal > 1_000_000 -> "Máximo RD$ 1,000,000"
            else -> null
        }
    }
}