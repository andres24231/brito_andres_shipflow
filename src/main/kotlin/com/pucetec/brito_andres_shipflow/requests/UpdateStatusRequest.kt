package com.pucetec.brito_andres_shipflow.requests

import com.pucetec.brito_andres_shipflow.models.entities.ShipmentStatus

data class UpdateStatusRequest(
    val status: ShipmentStatus,
    val comment: String? = null
)