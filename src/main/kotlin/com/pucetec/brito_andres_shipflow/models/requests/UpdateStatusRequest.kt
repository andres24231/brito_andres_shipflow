package com.pucetec.brito_andres_shipflow.models.requests

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class UpdateStatusRequest @Suppress("unused") constructor(
    @field:NotNull(message = "Status is required.")
    val status: com.pucetec.brito_andres_shipflow.models.entities.ShipmentStatus,
    @field:Size(max = 100, message = "Comment cannot exceed 100 characters.")
    val comment: String? = null
) 