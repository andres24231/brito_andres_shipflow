package com.pucetec.brito_andres_shipflow.models.responses

data class ShipmentHistoryResponse(
    val status: String,
    val occurredAt: String,
    val comment: String?
)