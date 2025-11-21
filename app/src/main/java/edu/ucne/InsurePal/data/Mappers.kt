package edu.ucne.InsurePal.data

import edu.ucne.InsurePal.data.remote.polizas.vehiculo.dto.SeguroVehiculoRequest
import edu.ucne.InsurePal.data.remote.polizas.vehiculo.dto.SeguroVehiculoResponse
import edu.ucne.InsurePal.data.remote.usuario.dto.UsuarioRequest
import edu.ucne.InsurePal.data.remote.usuario.dto.UsuarioResponse
import edu.ucne.InsurePal.domain.polizas.vehiculo.model.SeguroVehiculo
import edu.ucne.InsurePal.domain.usuario.model.Usuario


fun Usuario.toRequest(): UsuarioRequest = UsuarioRequest(
    userName = userName,
    password = password
)

fun UsuarioResponse.toDomain() = Usuario(
    usuarioId = usuarioId,
    userName = userName,
    password = password
)

fun SeguroVehiculoResponse.toDomain() = SeguroVehiculo(
    usuarioId = usuarioId,
    idPoliza = idPoliza,
    name = name,
    marca = marca,
    modelo = modelo,
    anio = anio,
    color = color,
    placa = placa,
    chasis = chasis,
    valorMercado = valorMercado,
    coverageType = coverageType,
    status = status ?: "Cotizando",
    expirationDate = expirationDate
)

fun SeguroVehiculo.toRequest(): SeguroVehiculoRequest = SeguroVehiculoRequest(
    usuarioId = usuarioId,
    name = name,
    marca = marca,
    modelo = modelo,
    anio = anio,
    color = color,
    placa = placa,
    chasis = chasis,
    valorMercado = valorMercado,
    coverageType = coverageType,
    status = status,
    expirationDate = expirationDate
)