package com.pucetec.brito_andres_shipflow.services

import com.pucetec.brito_andres_shipflow.exceptions.InvalidStatusTransitionException
import com.pucetec.brito_andres_shipflow.models.entities.Shipment
import com.pucetec.brito_andres_shipflow.models.entities.ShipmentHistory
import com.pucetec.brito_andres_shipflow.models.entities.ShipmentStatus
import com.pucetec.brito_andres_shipflow.models.requests.CreateShipmentRequest
import com.pucetec.brito_andres_shipflow.models.requests.UpdateStatusRequest
import com.pucetec.brito_andres_shipflow.models.responses.ShipmentResponse
import com.pucetec.brito_andres_shipflow.repositories.ShipmentHistoryRepository
import com.pucetec.brito_andres_shipflow.repositories.ShipmentRepository
import com.pucetec.brito_andres_shipflow.mappers.ShipmentMapper
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class ShipmentService(
    private val shipmentRepository: ShipmentRepository,
    private val shipmentHistoryRepository: ShipmentHistoryRepository,
    private val mapper: ShipmentMapper
) {

    fun createShipment(request: CreateShipmentRequest): ShipmentResponse {
        if (request.originCity == request.destinationCity) {
            throw IllegalArgumentException("Origin and destination cannot be the same.")
        }
        if (request.description.length > 50) {
            throw IllegalArgumentException("Description cannot exceed 50 characters.")
        }

        val trackingId = UUID.randomUUID().toString()

        val shipment = Shipment(
            trackingId = trackingId,
            status = ShipmentStatus.PENDING,
            type = request.type,
            description = request.description,
            weight = request.weight,
            originCity = request.originCity,
            destinationCity = request.destinationCity,
            createdAt = LocalDateTime.now(),
            estimatedDeliveryDate = LocalDateTime.now().plusDays(5)
        )

        val saved = shipmentRepository.save(shipment)

        // Register initial event
        shipmentHistoryRepository.save(
            ShipmentHistory(
                shipment = saved,
                status = ShipmentStatus.PENDING,
                occurredAt = LocalDateTime.now(),
                comment = "Shipment created"
            )
        )

        return mapper.toResponse(saved)
    }

    fun getShipmentByTrackingId(trackingId: String): ShipmentResponse {
        val shipment = shipmentRepository.findByTrackingId(trackingId)
            .orElseThrow { NoSuchElementException("Shipment not found.") }
        return mapper.toResponse(shipment)
    }

    fun getAllShipments(): List<ShipmentResponse> {
        return shipmentRepository.findAll().map { mapper.toResponse(it) }
    }

    fun getShipmentHistory(trackingId: String): List<com.pucetec.brito_andres_shipflow.models.responses.ShipmentHistoryResponse> {
        val shipment = shipmentRepository.findByTrackingId(trackingId)
            .orElseThrow { NoSuchElementException("Shipment not found.") }
        return shipment.history.map { mapper.toHistoryResponse(it) }
    }

    fun updateShipmentStatus(id: Long, request: UpdateStatusRequest): ShipmentResponse {
        val shipment = shipmentRepository.findById(id)
            .orElseThrow { NoSuchElementException("Shipment not found.") }

        validateStatusTransition(shipment, request.status)

        shipment.status = request.status
        val updated = shipmentRepository.save(shipment)

        shipmentHistoryRepository.save(
            ShipmentHistory(
                shipment = updated,
                status = request.status,
                occurredAt = LocalDateTime.now(),
                comment = request.comment
            )
        )

        return mapper.toResponse(updated)
    }

    private fun validateStatusTransition(shipment: Shipment, new: ShipmentStatus) {
        val current = shipment.status
        when (current) {
            ShipmentStatus.PENDING -> if (new != ShipmentStatus.IN_TRANSIT) throw InvalidStatusTransitionException("Invalid status transition.")
            ShipmentStatus.IN_TRANSIT -> if (new !in listOf(ShipmentStatus.DELIVERED, ShipmentStatus.ON_HOLD)) throw InvalidStatusTransitionException("Invalid status transition.")
            ShipmentStatus.ON_HOLD -> if (new !in listOf(ShipmentStatus.IN_TRANSIT, ShipmentStatus.CANCELLED)) throw InvalidStatusTransitionException("Invalid status transition.")
            ShipmentStatus.DELIVERED, ShipmentStatus.CANCELLED -> throw InvalidStatusTransitionException("Invalid status transition.")
        }
        // Validate DELIVERED only if previously IN_TRANSIT
        if (new == ShipmentStatus.DELIVERED) {
            val history = shipment.history.map { it.status }
            if (ShipmentStatus.IN_TRANSIT !in history) {
                throw InvalidStatusTransitionException("DELIVERED status can only be set if shipment was previously IN_TRANSIT.")
            }
        }
    }
}