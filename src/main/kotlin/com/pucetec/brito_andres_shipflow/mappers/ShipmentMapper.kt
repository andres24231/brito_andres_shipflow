package com.pucetec.brito_andres_shipflow.mappers

import com.pucetec.brito_andres_shipflow.models.entities.Shipment
import com.pucetec.brito_andres_shipflow.models.entities.ShipmentHistory
import com.pucetec.brito_andres_shipflow.models.responses.ShipmentResponse
import com.pucetec.brito_andres_shipflow.models.responses.ShipmentHistoryResponse
import org.springframework.stereotype.Component

@Component
class ShipmentMapper {
    fun toResponse(shipment: Shipment): ShipmentResponse {
        return ShipmentResponse(
            id = shipment.id!!,
            trackingId = shipment.trackingId,
            status = shipment.status.name,
            type = shipment.type,
            description = shipment.description,
            weight = shipment.weight,
            originCity = shipment.originCity,
            destinationCity = shipment.destinationCity,
            createdAt = shipment.createdAt.toString(),
            estimatedDeliveryDate = shipment.estimatedDeliveryDate.toString(),
            history = shipment.history.map {
                ShipmentHistoryResponse(
                    status = it.status.name,
                    occurredAt = it.occurredAt.toString(),
                    comment = it.comment
                )
            }
        )
    }

    fun toHistoryResponse(history: ShipmentHistory): ShipmentHistoryResponse {
        return ShipmentHistoryResponse(
            status = history.status.name,
            occurredAt = history.occurredAt.toString(),
            comment = history.comment
        )
    }
}