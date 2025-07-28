package com.pucetec.brito_andres_shipflow.services

import com.pucetec.brito_andres_shipflow.exceptions.InvalidStatusTransitionException
import com.pucetec.brito_andres_shipflow.mappers.ShipmentMapper
import com.pucetec.brito_andres_shipflow.models.entities.Shipment
import com.pucetec.brito_andres_shipflow.models.entities.ShipmentHistory
import com.pucetec.brito_andres_shipflow.models.entities.ShipmentStatus
import com.pucetec.brito_andres_shipflow.models.requests.CreateShipmentRequest
import com.pucetec.brito_andres_shipflow.models.requests.UpdateStatusRequest
import com.pucetec.brito_andres_shipflow.models.responses.ShipmentResponse
import com.pucetec.brito_andres_shipflow.repositories.ShipmentHistoryRepository
import com.pucetec.brito_andres_shipflow.repositories.ShipmentRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class ShipmentServiceTest {
    @Mock
    private lateinit var shipmentRepository: ShipmentRepository
    @Mock
    private lateinit var shipmentHistoryRepository: ShipmentHistoryRepository
    @Mock
    private lateinit var mapper: ShipmentMapper
    @InjectMocks
    private lateinit var shipmentService: ShipmentService

    private val fixedDate = LocalDateTime.of(2023, 1, 1, 12, 0)
    private val fixedDelivery = fixedDate.plusDays(5)

    @Test
    fun create_shipment_success() {
        val fixedDate = LocalDateTime.of(2023, 1, 1, 12, 0)
        val fixedDelivery = fixedDate.plusDays(5)
        val request = CreateShipmentRequest("type", "desc", 1.0, "A", "B")
        val shipment = Shipment(
            id = 1L, trackingId = "track", status = ShipmentStatus.PENDING, type = "type", description = "desc", weight = 1.0,
            originCity = "A", destinationCity = "B", createdAt = fixedDate, estimatedDeliveryDate = fixedDelivery,
            history = mutableListOf()
        )
        val response = ShipmentResponse(1L, "track", "PENDING", "type", "desc", 1.0, "A", "B", fixedDate.toString(), fixedDelivery.toString(), listOf())
        whenever(shipmentRepository.save(any<Shipment>())).thenReturn(shipment)
        whenever(shipmentHistoryRepository.save(any<ShipmentHistory>())).thenReturn(mock())
        whenever(mapper.toResponse(any())).thenReturn(response)
        val result = shipmentService.createShipment(request)
        assertEquals(response, result)
    }

    @Test
    fun create_shipment_fail_same_origin_destination() {
        val request = CreateShipmentRequest("type", "desc", 1.0, "A", "A")
        val ex = assertThrows<IllegalArgumentException> { shipmentService.createShipment(request) }
        assertEquals("Origin and destination cannot be the same.", ex.message)
    }

    @Test
    fun create_shipment_fail_long_description() {
        val request = CreateShipmentRequest("type", "a".repeat(51), 1.0, "A", "B")
        val ex = assertThrows<IllegalArgumentException> { shipmentService.createShipment(request) }
        assertEquals("Description cannot exceed 50 characters.", ex.message)
    }

    @Test
    fun get_shipment_by_tracking_id_success() {
        val shipment = Shipment(
            id = 1L, trackingId = "track", status = ShipmentStatus.PENDING, type = "type", description = "desc", weight = 1.0,
            originCity = "A", destinationCity = "B", createdAt = fixedDate, estimatedDeliveryDate = fixedDelivery,
            history = mutableListOf()
        )
        val response = ShipmentResponse(1L, "track", "PENDING", "type", "desc", 1.0, "A", "B", fixedDate.toString(), fixedDelivery.toString(), listOf())
        whenever(shipmentRepository.findByTrackingId("track")).thenReturn(Optional.of(shipment))
        whenever(mapper.toResponse(any())).thenReturn(response)
        val result = shipmentService.getShipmentByTrackingId("track")
        assertEquals(response, result)
    }

    @Test
    fun get_shipment_by_tracking_id_not_found() {
        whenever(shipmentRepository.findByTrackingId("nope")).thenReturn(Optional.empty())
        val ex = assertThrows<NoSuchElementException> { shipmentService.getShipmentByTrackingId("nope") }
        assertEquals("Shipment not found.", ex.message)
    }

    @Test
    fun get_all_shipments() {
        val shipment = Shipment(
            id = 1L, trackingId = "track", status = ShipmentStatus.PENDING, type = "type", description = "desc", weight = 1.0,
            originCity = "A", destinationCity = "B", createdAt = fixedDate, estimatedDeliveryDate = fixedDelivery,
            history = mutableListOf()
        )
        val response = ShipmentResponse(1L, "track", "PENDING", "type", "desc", 1.0, "A", "B", fixedDate.toString(), fixedDelivery.toString(), listOf())
        whenever(shipmentRepository.findAll()).thenReturn(listOf(shipment))
        whenever(mapper.toResponse(any())).thenReturn(response)
        val result = shipmentService.getAllShipments()
        assertEquals(listOf(response), result)
    }

    @Test
    fun get_shipment_history_success() {
        val history = mutableListOf(
            ShipmentHistory(
                id = 1L,
                shipment = mock(),
                status = ShipmentStatus.PENDING,
                occurredAt = LocalDateTime.now(),
                comment = "Creado"
            )
        )
        val shipment = Shipment(
            id = 1L, trackingId = "track", status = ShipmentStatus.PENDING, type = "type", description = "desc", weight = 1.0,
            originCity = "A", destinationCity = "B", createdAt = fixedDate, estimatedDeliveryDate = fixedDelivery,
            history = history
        )
        val historyResponse = com.pucetec.brito_andres_shipflow.models.responses.ShipmentHistoryResponse("PENDING", history[0].occurredAt.toString(), "Creado")
        whenever(shipmentRepository.findByTrackingId("track")).thenReturn(Optional.of(shipment))
        whenever(mapper.toHistoryResponse(any())).thenReturn(historyResponse)
        val result = shipmentService.getShipmentHistory("track")
        assertEquals(listOf(historyResponse), result)
    }

    @Test
    fun get_shipment_history_not_found() {
        whenever(shipmentRepository.findByTrackingId("nope")).thenReturn(Optional.empty())
        val ex = assertThrows<NoSuchElementException> { shipmentService.getShipmentHistory("nope") }
        assertEquals("Shipment not found.", ex.message)
    }

    @Test
    fun update_status_pending_to_in_transit() {
        val shipment = Shipment(
            id = 1L, trackingId = "track", status = ShipmentStatus.PENDING, type = "type", description = "desc", weight = 1.0,
            originCity = "A", destinationCity = "B", createdAt = fixedDate, estimatedDeliveryDate = fixedDelivery,
            history = mutableListOf()
        )
        val request = UpdateStatusRequest(ShipmentStatus.IN_TRANSIT, "En transito")
        val updated = shipment.copy(status = ShipmentStatus.IN_TRANSIT)
        val response = ShipmentResponse(1L, "track", "IN_TRANSIT", "type", "desc", 1.0, "A", "B", fixedDate.toString(), fixedDelivery.toString(), listOf())
        whenever(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment))
        whenever(shipmentRepository.save(any<Shipment>())).thenReturn(updated)
        whenever(shipmentHistoryRepository.save(any<ShipmentHistory>())).thenReturn(mock())
        whenever(mapper.toResponse(any())).thenReturn(response)
        val result = shipmentService.updateShipmentStatus(1L, request)
        assertEquals(response, result)
    }

    @Test
    fun update_status_not_found() {
        val request = UpdateStatusRequest(ShipmentStatus.IN_TRANSIT, "En transito")
        whenever(shipmentRepository.findById(1L)).thenReturn(Optional.empty())
        val ex = assertThrows<NoSuchElementException> { shipmentService.updateShipmentStatus(1L, request) }
        assertEquals("Shipment not found.", ex.message)
    }

    @Test
    fun update_status_invalid_transition() {
        val shipment = Shipment(
            id = 1L, trackingId = "track", status = ShipmentStatus.PENDING, type = "type", description = "desc", weight = 1.0,
            originCity = "A", destinationCity = "B", createdAt = fixedDate, estimatedDeliveryDate = fixedDelivery,
            history = mutableListOf()
        )
        val request = UpdateStatusRequest(ShipmentStatus.DELIVERED, "Entregado")
        whenever(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment))
        val ex = assertThrows<InvalidStatusTransitionException> { shipmentService.updateShipmentStatus(1L, request) }
        assertTrue(ex.message!!.contains("Invalid status transition."))
    }

    @Test
    fun update_status_to_delivered_without_in_transit() {
        val shipment = Shipment(
            id = 1L, trackingId = "track", status = ShipmentStatus.IN_TRANSIT, type = "type", description = "desc", weight = 1.0,
            originCity = "A", destinationCity = "B", createdAt = fixedDate, estimatedDeliveryDate = fixedDelivery,
            history = mutableListOf()
        )
        val request = UpdateStatusRequest(ShipmentStatus.DELIVERED, "Entregado")
        whenever(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment))
        val ex = assertThrows<InvalidStatusTransitionException> { shipmentService.updateShipmentStatus(1L, request) }
        assertTrue(ex.message!!.contains("DELIVERED status can only be set if shipment was previously IN_TRANSIT."))
    }
}