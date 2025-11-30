package edu.ucne.InsurePal.data.repositoryTest

import edu.ucne.InsurePal.data.Resource
import edu.ucne.InsurePal.data.remote.reclamoVehiculo.ReclamoCreateRequest
import edu.ucne.InsurePal.data.remote.reclamoVehiculo.ReclamoRemoteDataSource
import edu.ucne.InsurePal.data.remote.reclamoVehiculo.ReclamoResponse
import edu.ucne.InsurePal.data.remote.reclamoVehiculo.ReclamoUpdateRequest
import edu.ucne.InsurePal.data.remote.reclamoVehiculo.ReclamoVehiculoRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File


class ReclamoVehiculoRepositoryImplTest {

    private val remoteDataSource: ReclamoRemoteDataSource = mockk()
    private lateinit var repository: ReclamoVehiculoRepositoryImpl

    @Before
    fun setUp() {
        repository = ReclamoVehiculoRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `crearReclamoVehiculo retorna Success y mapea el request correctamente`() = runTest {
        val fileMock = mockk<File>()
        val fakeResponse = ReclamoResponse(
            id = "REC-V-001",
            folio = "F-123",
            polizaId = "P-123",
            usuarioId = 1,
            descripcion = "Choque frontal",
            direccion = "Av. Independencia",
            tipoIncidente = "Colisión",
            fechaIncidente = "2023-11-29",
            imagenUrl = "http://img.com/1.jpg",
            status = "PENDIENTE",
            numCuenta = "999-999",
            motivoRechazo = null,
            fechaCreacion = "2023-11-29",
            fechaActualizacion = null
        )

        coEvery {
            remoteDataSource.crearReclamo(any(), any())
        } returns Resource.Success(fakeResponse)

        val requestSlot = slot<ReclamoCreateRequest>()

        val result = repository.crearReclamoVehiculo(
            polizaId = "P-123",
            usuarioId = 1,
            descripcion = "Choque frontal",
            direccion = "Av. Independencia",
            tipoIncidente = "Colisión",
            fechaIncidente = "2023-11-29",
            numCuenta = "999-999",
            imagen = fileMock
        )

        coVerify {
            remoteDataSource.crearReclamo(capture(requestSlot), fileMock)
        }

        val capturedRequest = requestSlot.captured
        assertEquals("P-123", capturedRequest.polizaId)
        assertEquals("Colisión", capturedRequest.tipoIncidente)
        assertEquals("Av. Independencia", capturedRequest.direccion)
        assertEquals("999-999", capturedRequest.numCuenta)
        assertEquals(1, capturedRequest.usuarioId)

        assertTrue(result is Resource.Success)
        assertEquals("REC-V-001", result.data?.id)
        assertEquals("PENDIENTE", result.data?.status)
    }

    @Test
    fun `crearReclamoVehiculo retorna Error cuando el datasource falla`() = runTest {
        val fileMock = mockk<File>()
        val errorMessage = "Error de conexión"

        coEvery {
            remoteDataSource.crearReclamo(any(), any())
        } returns Resource.Error(errorMessage)

        val result = repository.crearReclamoVehiculo(
            polizaId = "", usuarioId = 0, descripcion = "",
            direccion = "", tipoIncidente = "", fechaIncidente = "",
            numCuenta = "", imagen = fileMock
        )

        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, result.message)
    }

    @Test
    fun `cambiarEstadoReclamoVehiculo retorna Success y mapea el update request`() = runTest {
        val fakeResponse = ReclamoResponse(
            id = "REC-V-001",
            folio = null,
            polizaId = "P-123",
            usuarioId = 1,
            descripcion = "Desc",
            direccion = "Dir",
            tipoIncidente = "Tipo",
            fechaIncidente = "Fecha",
            imagenUrl = "Url",
            status = "APROBADO",
            numCuenta = "123",
            motivoRechazo = null,
            fechaCreacion = "Fecha",
            fechaActualizacion = null
        )

        coEvery {
            remoteDataSource.updateEstado(any(), any())
        } returns Resource.Success(fakeResponse)

        val slotUpdate = slot<ReclamoUpdateRequest>()

        val result = repository.cambiarEstadoReclamoVehiculo(
            reclamoId = "REC-V-001",
            nuevoEstado = "APROBADO",
            motivoRechazo = null
        )

        coVerify { remoteDataSource.updateEstado(eq("REC-V-001"), capture(slotUpdate)) }

        assertEquals("APROBADO", slotUpdate.captured.status)
        assertEquals(null, slotUpdate.captured.motivoRechazo)

        assertTrue(result is Resource.Success)
        assertEquals("APROBADO", result.data?.status)
    }

    @Test
    fun `obtenerReclamoVehiculos retorna lista mapeada correctamente`() = runTest {
        val listResponse = listOf(
            ReclamoResponse(
                id = "1",
                folio = null,
                polizaId = "P1",
                usuarioId = 1,
                descripcion = "A",
                direccion = "D",
                tipoIncidente = "T",
                fechaIncidente = "F",
                imagenUrl = "U",
                status = "S",
                numCuenta = "N",
                motivoRechazo = null,
                fechaCreacion = "FC",
                fechaActualizacion = null
            ),
            ReclamoResponse(
                id = "2",
                folio = null,
                polizaId = "P1",
                usuarioId = 1,
                descripcion = "B",
                direccion = "D",
                tipoIncidente = "T",
                fechaIncidente = "F",
                imagenUrl = "U",
                status = "S",
                numCuenta = "N",
                motivoRechazo = null,
                fechaCreacion = "FC",
                fechaActualizacion = null
            )
        )

        coEvery { remoteDataSource.getReclamos(any()) } returns Resource.Success(listResponse)

        val result = repository.obtenerReclamoVehiculos(1)

        assertTrue(result is Resource.Success)
        assertEquals(2, result.data?.size)
        assertEquals("A", result.data?.get(0)?.descripcion)
        assertEquals("B", result.data?.get(1)?.descripcion)
    }

    @Test
    fun `obtenerReclamoVehiculoPorId retorna Success`() = runTest {
        val fakeResponse = ReclamoResponse(
            id = "TargetId",
            folio = null,
            polizaId = "P-123",
            usuarioId = 1,
            descripcion = "Found",
            direccion = "Dir",
            tipoIncidente = "Type",
            fechaIncidente = "Date",
            imagenUrl = "Img",
            status = "Stat",
            numCuenta = "Num",
            motivoRechazo = null,
            fechaCreacion = "FC",
            fechaActualizacion = null
        )

        coEvery { remoteDataSource.getReclamo("TargetId") } returns Resource.Success(fakeResponse)

        val result = repository.obtenerReclamoVehiculoPorId("TargetId")

        assertTrue(result is Resource.Success)
        assertEquals("TargetId", result.data?.id)
        assertEquals("Found", result.data?.descripcion)
    }
}