package edu.ucne.InsurePal.domain.polizas.vehiculo.useCases

import edu.ucne.InsurePal.domain.polizas.vehiculo.model.MarcaVehiculo
import edu.ucne.InsurePal.domain.polizas.vehiculo.repository.SeguroVehiculoRepository
import jakarta.inject.Inject
import java.util.Calendar
import kotlin.math.max

class CalcularValorVehiculoUseCase @Inject constructor(
    private val repository: SeguroVehiculoRepository
) {
    operator fun invoke(
        marca: String,
        modelo: String,
        anio: String,
        catalogo: List<MarcaVehiculo>
    ): Double {

        val anioVehiculo = anio.toIntOrNull() ?: return 0.0

        if (modelo.isBlank()) return 0.0

        // Buscamos el precio base en la lista que nos pasan
        val precioBase = catalogo
            .find { it.nombre.equals(marca, ignoreCase = true) }
            ?.modelos
            ?.find { it.nombre.equals(modelo, ignoreCase = true) }
            ?.precioBase ?: return 0.0

        val anioActual = Calendar.getInstance().get(Calendar.YEAR)

        // Si es del año actual o futuro, no se deprecia
        if (anioVehiculo >= anioActual) return precioBase

        val antiguedad = anioActual - anioVehiculo
        val porcentajeDepreciacion = antiguedad * 0.05 // 5% por año
        val valorDescontado = precioBase * (1.0 - porcentajeDepreciacion)

        // El valor nunca baja del 20% del precio base
        val valorMinimo = precioBase * 0.20

        return max(valorDescontado, valorMinimo)
    }
}