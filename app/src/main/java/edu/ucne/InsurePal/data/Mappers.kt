package edu.ucne.InsurePal.data

import edu.ucne.InsurePal.data.remote.usuario.dto.UsuarioRequest
import edu.ucne.InsurePal.data.remote.usuario.dto.UsuarioResponse
import edu.ucne.InsurePal.domain.usuario.Usuario

fun Usuario.toRequest(): UsuarioRequest = UsuarioRequest(
    userName = userName,
    password = password
)

fun UsuarioResponse.toDomain() = Usuario(
    usuarioId = usuarioId,
    userName = userName,
    password = password
)