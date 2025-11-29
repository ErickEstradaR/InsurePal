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

        val precioBase = catalogo
            .find { it.nombre.equals(marca, ignoreCase = true) }
            ?.modelos
            ?.find { it.nombre.equals(modelo, ignoreCase = true) }
            ?.precioBase ?: return 0.0

        val anioActual = Calendar.getInstance()[Calendar.YEAR]

        if (anioVehiculo >= anioActual) return precioBase

        val antiguedad = anioActual - anioVehiculo
        val porcentajeDepreciacion = antiguedad * 0.05
        val valorDescontado = precioBase * (1.0 - porcentajeDepreciacion)


        val valorMinimo = precioBase * 0.20

        return max(valorDescontado, valorMinimo)
    }
}