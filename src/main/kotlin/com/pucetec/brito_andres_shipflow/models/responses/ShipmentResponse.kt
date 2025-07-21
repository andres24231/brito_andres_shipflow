package com.pucetec.brito_andres_shipflow.models.responses

data class ShipmentResponse(
    val id: Long,
    val trackingId: String,
    val status: String,
    val type: String,
    val description: String,
    val weight: Double,
    val originCity: String,
    val destinationCity: String,
    val createdAt: String,
    val estimatedDeliveryDate: String,
    val history: List<ShipmentHistoryResponse>
)